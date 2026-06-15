package top.diaoyugan.veinmine.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import top.diaoyugan.veinmine.utils.logging.Logger;
import top.diaoyugan.veinmine.utils.logging.LoggerLevels;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SmartVein {
    private static final BlockPos[] OFFSETS = createOffsets();
    private static final List<String> DYE_PREFIXES = DyeColor.VALUES.stream()
            .map(color -> color.getName() + "_")
            .sorted(Comparator.comparingInt(String::length).reversed())
            .toList();
    private static final Map<Block, Optional<Identifier>> DYED_FAMILY_CACHE = new ConcurrentHashMap<>();
    private static final Map<Block, Optional<Identifier>> ORE_FAMILY_CACHE = new ConcurrentHashMap<>();

    static {
        try {
            Utils.getConfig().ignoredBlocks.add("minecraft:air");
        } catch (Exception exception) {
            if (!(exception instanceof UnsupportedOperationException)) {
                Logger.throwLog(LoggerLevels.ERROR, String.valueOf(exception), exception.fillInStackTrace());
            }
        }
    }

    public static List<BlockPos> findBlocks(Level world, BlockPos startPos) {
        return findBlocks(world, startPos, world.getBlockState(startPos));
    }

    public static List<BlockPos> findBlocks(Level world, BlockPos startPos, BlockState targetState) {
        if (isIgnoredTargetType(targetState.getBlock()) || !Utils.getConfig().useBFS) {
            return findBlocksInCube(world, startPos, targetState);
        }
        return findConnectedBlocks(world, startPos, targetState);
    }

    private static List<BlockPos> findBlocksInCube(Level world, BlockPos centerPos, BlockState targetState) {
        if (!Utils.getConfig().useRadiusSearch) return null;

        List<BlockPos> foundBlocks = new ArrayList<>();
        int radius = Utils.getConfig().searchRadius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = centerPos.offset(x, y, z);
                    if (matchesTarget(targetState, world.getBlockState(targetPos))) {
                        foundBlocks.add(targetPos);
                    }
                }
            }
        }
        return foundBlocks;
    }

    private static List<BlockPos> findConnectedBlocks(Level world, BlockPos startPos, BlockState targetState) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);

            if (foundBlocks.size() > Utils.getConfig().BFSLimit) {
                if (Utils.getConfig().useRadiusSearchWhenReachBFSLimit) {
                    return findBlocksInCube(world, startPos, targetState);
                }
                return null;
            }

            for (BlockPos offset : OFFSETS) {
                BlockPos neighborPos = current.offset(offset);
                if (!visited.add(neighborPos)) continue;

                if (matchesTarget(targetState, world.getBlockState(neighborPos))) {
                    queue.add(neighborPos);
                    continue;
                }

                if (Utils.getConfig().bridgeOneBlockGap) {
                    BlockPos bridgedPos = neighborPos.offset(offset);
                    if (visited.add(bridgedPos)
                            && matchesTarget(targetState, world.getBlockState(bridgedPos))) {
                        queue.add(bridgedPos);
                    }
                }
            }
        }
        return foundBlocks;
    }

    public static boolean matchesTarget(BlockState targetState, BlockState candidateState) {
        if (!matchesBlockType(targetState.getBlock(), candidateState.getBlock())) return false;

        if (Utils.getConfig().distinguishCropMaturity
                && targetState.is(BlockTags.CROPS)
                && candidateState.is(BlockTags.CROPS)) {
            return isMatureCrop(targetState) == isMatureCrop(candidateState);
        }
        return true;
    }

    private static boolean matchesBlockType(Block targetBlock, Block candidateBlock) {
        if (targetBlock == candidateBlock) return true;
        if (!Utils.getConfig().distinguishDeepslateOres) {
            Optional<Identifier> targetOreFamily = oreFamily(targetBlock);
            if (targetOreFamily.isPresent() && targetOreFamily.equals(oreFamily(candidateBlock))) {
                return true;
            }
        }
        if (Utils.getConfig().distinguishDyedBlockColors) return false;

        Optional<Identifier> targetFamily = dyedFamily(targetBlock);
        return targetFamily.isPresent() && targetFamily.equals(dyedFamily(candidateBlock));
    }

    private static boolean isIgnoredTargetType(Block targetBlock) {
        Set<String> ignoredBlocks = Utils.getConfig().ignoredBlocks;
        Identifier targetId = BuiltInRegistries.BLOCK.getKey(targetBlock);
        if (ignoredBlocks.contains(targetId.toString())) return true;

        if (!Utils.getConfig().distinguishDeepslateOres) {
            Optional<Identifier> oreFamily = oreFamily(targetBlock);
            if (oreFamily.isPresent()) {
                Identifier normalOreId = oreFamily.get();
                Identifier deepslateOreId = Identifier.fromNamespaceAndPath(
                        normalOreId.getNamespace(),
                        "deepslate_" + normalOreId.getPath()
                );
                if (ignoredBlocks.contains(normalOreId.toString())
                        || ignoredBlocks.contains(deepslateOreId.toString())) {
                    return true;
                }
            }
        }

        if (!Utils.getConfig().distinguishDyedBlockColors) {
            Optional<Identifier> dyedFamily = dyedFamily(targetBlock);
            if (dyedFamily.isPresent()) {
                Identifier familyId = dyedFamily.get();
                return DyeColor.VALUES.stream()
                        .map(color -> Identifier.fromNamespaceAndPath(
                                familyId.getNamespace(),
                                color.getName() + "_" + familyId.getPath()
                        ).toString())
                        .anyMatch(ignoredBlocks::contains);
            }
        }
        return false;
    }

    private static boolean isMatureCrop(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            if (property instanceof IntegerProperty ageProperty && property.getName().equals("age")) {
                List<Integer> ages = ageProperty.getPossibleValues();
                return state.getValue(ageProperty).equals(ages.getLast());
            }
        }
        return false;
    }

    private static Optional<Identifier> dyedFamily(Block block) {
        return DYED_FAMILY_CACHE.computeIfAbsent(block, SmartVein::findDyedFamily);
    }

    private static Optional<Identifier> oreFamily(Block block) {
        return ORE_FAMILY_CACHE.computeIfAbsent(block, SmartVein::findOreFamily);
    }

    private static Optional<Identifier> findOreFamily(Block block) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
        String path = blockId.getPath();
        String normalOrePath;
        String deepslateOrePath;

        if (path.startsWith("deepslate_") && path.endsWith("_ore")) {
            normalOrePath = path.substring("deepslate_".length());
            deepslateOrePath = path;
        } else if (path.endsWith("_ore")) {
            normalOrePath = path;
            deepslateOrePath = "deepslate_" + path;
        } else {
            return Optional.empty();
        }

        Identifier normalOreId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), normalOrePath);
        Identifier deepslateOreId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), deepslateOrePath);
        if (BuiltInRegistries.BLOCK.containsKey(normalOreId)
                && BuiltInRegistries.BLOCK.containsKey(deepslateOreId)) {
            return Optional.of(normalOreId);
        }
        return Optional.empty();
    }

    private static Optional<Identifier> findDyedFamily(Block block) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
        String path = blockId.getPath();

        for (String prefix : DYE_PREFIXES) {
            if (!path.startsWith(prefix)) continue;

            String familyPath = path.substring(prefix.length());
            boolean hasAllColors = DyeColor.VALUES.stream()
                    .map(color -> Identifier.fromNamespaceAndPath(
                            blockId.getNamespace(),
                            color.getName() + "_" + familyPath
                    ))
                    .allMatch(BuiltInRegistries.BLOCK::containsKey);

            if (hasAllColors) {
                return Optional.of(Identifier.fromNamespaceAndPath(blockId.getNamespace(), familyPath));
            }
        }
        return Optional.empty();
    }

    private static BlockPos[] createOffsets() {
        List<BlockPos> offsets = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        offsets.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return offsets.toArray(new BlockPos[0]);
    }
}

package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.utils.logging.Logger;
import top.diaoyugan.vein_mine.utils.logging.LoggerLevels;

import java.util.*;


/**
 * 智能连锁挖掘工具类。
 * <p>
 * 提供根据玩家起始位置查找相同方块的功能，支持立方体搜索和基于 BFS 的智能连锁搜索。
 */
public class SmartVein {

    static {
        try {
            Utils.getConfig().ignoredBlocks.add("minecraft:air");
        } catch (Exception e) { // TODO: 找到更好的方式处理
            if (!(e instanceof UnsupportedOperationException)) {
                Logger.throwLog(LoggerLevels.ERROR, String.valueOf(e), e.fillInStackTrace());
            }
        }
    }

    /**
     * 根据起始方块查找相同方块。
     *
     * @param world    世界对象
     * @param startPos 起始方块位置
     * @return 找到的方块位置列表
     */
    public static List<BlockPos> findBlocks(World world, BlockPos startPos) {
        BlockState startState = world.getBlockState(startPos);
        Identifier blockID = Registries.BLOCK.getId(startState.getBlock());
        String startBlockID = blockID.toString();

        if (Utils.getConfig().ignoredBlocks.contains(startBlockID) || !Utils.getConfig().useBFS) {
            return findBlocksInCube(world, startPos, startState);
        } else {
            return findConnectedBlocks(world, startPos, startState);
        }
    }

    /**
     * 根据起始方块 ID 查找相同方块（重载方法）。
     *
     * @param world        世界对象
     * @param startPos     起始方块位置
     * @param startBlockID 起始方块 ID
     * @return 找到的方块位置列表
     */
    public static List<BlockPos> findBlocks(World world, BlockPos startPos, Identifier startBlockID) {
        if (Utils.getConfig().ignoredBlocks.contains(String.valueOf(startBlockID)) || !Utils.getConfig().useBFS) {
            return findBlocksInCube(world, startPos, startBlockID);
        } else {
            return findConnectedBlocks(world, startPos, startBlockID);
        }
    }

    /**
     * 立方体范围查找相同方块。
     *
     * @param world       世界对象
     * @param pos         中心位置
     * @param targetState 目标方块State
     * @return 找到的方块位置列表
     */
    private static List<BlockPos> findBlocksInCube(World world, BlockPos pos, BlockState targetState) {
        if (!Utils.getConfig().useRadiusSearch) return null;

        List<BlockPos> foundBlocks = new ArrayList<>();
        int radius = Utils.getConfig().searchRadius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.add(x, y, z);
                    if (world.getBlockState(targetPos).getBlock() == targetState.getBlock()) {
                        foundBlocks.add(targetPos);
                    }
                }
            }
        }

        return foundBlocks;
    }

    /**
     * 立方体范围查找相同方块（使用方块 ID）。
     *
     * @param world        世界对象
     * @param pos          中心位置
     * @param startBlockID 目标方块 ID
     * @return 找到的方块位置列表
     */
    private static List<BlockPos> findBlocksInCube(World world, BlockPos pos, Identifier startBlockID) {
        if (!Utils.getConfig().useRadiusSearch) return null;

        List<BlockPos> foundBlocks = new ArrayList<>();
        Block block = Registries.BLOCK.get(startBlockID);
        int radius = Utils.getConfig().searchRadius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.add(x, y, z);
                    if (world.getBlockState(targetPos).getBlock() == block) {
                        foundBlocks.add(targetPos);
                    }
                }
            }
        }

        return foundBlocks;
    }

    /**
     * 使用 BFS 算法进行智能连锁查找。
     *
     * @param world       世界对象
     * @param startPos    起始方块位置
     * @param targetState 目标方块State
     * @return 找到的方块位置列表
     */
    private static List<BlockPos> findConnectedBlocks(World world, BlockPos startPos, BlockState targetState) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        queue.add(startPos);
        visited.add(startPos);
        int connectedCount = 0;

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);
            connectedCount++;

            if (connectedCount > Utils.getConfig().BFSLimit) {
                if (Utils.getConfig().useRadiusSearchWhenReachBFSLimit) {
                    return findBlocksInCube(world, startPos, targetState);
                } else {
                    return null;
                }
            }

            for (BlockPos offset : getAllNeighborOffsets()) {
                BlockPos neighborPos = current.add(offset);
                if (!visited.contains(neighborPos) && isSameBlock(world, targetState, neighborPos)) {
                    queue.add(neighborPos);
                    visited.add(neighborPos);
                }
            }
        }

        return foundBlocks;
    }

    /**
     * 使用 BFS 算法进行智能连锁查找（通过方块 ID）。
     *
     * @param world        世界对象
     * @param startPos     起始方块位置
     * @param startBlockID 目标方块 ID
     * @return 找到的方块位置列表
     */
    private static List<BlockPos> findConnectedBlocks(World world, BlockPos startPos, Identifier startBlockID) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        Block block = Registries.BLOCK.get(startBlockID);
        BlockState startState = block.getDefaultState();

        queue.add(startPos);
        visited.add(startPos);
        int connectedCount = 0;

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);
            connectedCount++;

            if (connectedCount > Utils.getConfig().BFSLimit) {
                if (Utils.getConfig().useRadiusSearchWhenReachBFSLimit) {
                    return findBlocksInCube(world, startPos, startBlockID);
                } else {
                    return null;
                }
            }

            for (BlockPos offset : getAllNeighborOffsets()) {
                BlockPos neighborPos = current.add(offset);
                if (!visited.contains(neighborPos) && isSameBlock(world, startState, neighborPos)) {
                    queue.add(neighborPos);
                    visited.add(neighborPos);
                }
            }
        }

        return foundBlocks;
    }

    /**
     * 获取相对 26 个方向的偏移量。
     *
     * @return 偏移量列表
     */
    private static List<BlockPos> getAllNeighborOffsets() {
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
        return offsets;
    }

    /**
     * 判断指定位置的方块是否和目标方块相同。
     *
     * @param world      世界对象
     * @param targetState 目标方块状态
     * @param pos        方块位置
     * @return true 表示相同，false 表示不同
     */
    private static boolean isSameBlock(World world, BlockState targetState, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == targetState.getBlock();
    }
}
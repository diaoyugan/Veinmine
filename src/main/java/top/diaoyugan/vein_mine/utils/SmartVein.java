package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.Config;
import top.diaoyugan.vein_mine.ConfigItems;

import java.util.*;


public class SmartVein {
    static ConfigItems config = new Config().getConfigItems();
    private static final int SEARCH_RADIUS = Utils.searchRadius;
    private static final int MAX_CONNECTED_BLOCKS = Utils.bfsLimit;

    private static final Set<String> IGNORED_BLOCKS = config.ignoredBlocks;

    public static List<BlockPos> findBlocks(World world, BlockPos startPos) {
        BlockState startState = world.getBlockState(startPos);

        // 获取方块的命名空间 ID
        Identifier blockID = Registries.BLOCK.getId(startState.getBlock());
        String startBlockID = blockID.toString();

        // 如果是排除列表中的方块，或者bfs被禁用，使用旧的范围查找
        if (IGNORED_BLOCKS.contains(startBlockID)||!config.useBFS) {
            return findBlocksInCube(world, startPos, startState);
        } else {
            // 否则，使用智能查找
            return findConnectedBlocks(world, startPos, startState);
        }
    }
    // 重载方法：接受原始方块ID
    public static List<BlockPos> findBlocks(World world, BlockPos startPos, Identifier startBlockID) {
        // 如果是排除列表中的方块，或者bfs被禁用，使用旧的范围查找
        if (IGNORED_BLOCKS.contains(String.valueOf(startBlockID))||!config.useBFS) {
            return findBlocksInCube(world, startPos, startBlockID);
        } else {
            // 否则，使用智能查找
            return findConnectedBlocks(world, startPos, startBlockID);
        }
    }

    //立方体查找（用于常见方块）
    private static List<BlockPos> findBlocksInCube(World world, BlockPos pos, BlockState targetState) {
        if (config.useRadiusSearch) {
            List<BlockPos> foundBlocks = new ArrayList<>();

            for (int x = -SmartVein.SEARCH_RADIUS; x <= SmartVein.SEARCH_RADIUS; x++) {
                for (int y = -SmartVein.SEARCH_RADIUS; y <= SmartVein.SEARCH_RADIUS; y++) {
                    for (int z = -SmartVein.SEARCH_RADIUS; z <= SmartVein.SEARCH_RADIUS; z++) {
                        BlockPos targetPos = pos.add(x, y, z);
                        if (world.getBlockState(targetPos).getBlock() == targetState.getBlock()) {
                            foundBlocks.add(targetPos);
                        }
                    }
                }
            }
            return foundBlocks;
        }else{
            return null;
        }
    }

    // 重载方法：接收方块 ID 来进行立方体查找
    private static List<BlockPos> findBlocksInCube(World world, BlockPos pos, Identifier startBlockID) {
        if (config.useRadiusSearch) {
            List<BlockPos> foundBlocks = new ArrayList<>();

            // 获取方块对象
            Block block = Registries.BLOCK.get(startBlockID);

            for (int x = -SmartVein.SEARCH_RADIUS; x <= SmartVein.SEARCH_RADIUS; x++) {
                for (int y = -SmartVein.SEARCH_RADIUS; y <= SmartVein.SEARCH_RADIUS; y++) {
                    for (int z = -SmartVein.SEARCH_RADIUS; z <= SmartVein.SEARCH_RADIUS; z++) {
                        BlockPos targetPos = pos.add(x, y, z);
                        if (world.getBlockState(targetPos).getBlock() == block) {
                            foundBlocks.add(targetPos);
                        }
                    }
                }
            }
            return foundBlocks;
        }else{
            return null;
        }
    }
    //智能连锁查找（BFS + 26 方向搜索）
    private static List<BlockPos> findConnectedBlocks(World world, BlockPos startPos,BlockState targetState) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        BlockState startState = world.getBlockState(startPos);

        queue.add(startPos);
        visited.add(startPos);

        int connectedCount = 0; // 用来记录已找到的相同方块数

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);
            connectedCount++; // 每找到一个相同方块，计数器加1

            // 如果查找到的相同方块超过MAX_CONNECTED_BLOCKS个，跳出并进行立方体查找
            if (connectedCount > MAX_CONNECTED_BLOCKS) {
                if(config.useRadiusSearchWhenReachBFSLimit){
                // 调用立方体查找方法，返回结果
                return findBlocksInCube(world, startPos,targetState);
                }else{
                    return null;
                }
            }

            // 遍历 26 个方向
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
    // 重载方法：接收方块 ID 进行智能连锁查找，不再检查起始位置的方块，而是直接使用传入的方块 ID
    private static List<BlockPos> findConnectedBlocks(World world, BlockPos startPos, Identifier startBlockID) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        // 直接使用传入的方块 ID 来判断起始位置的方块是否匹配
        Block block = Registries.BLOCK.get(startBlockID); // 使用传入的方块ID获取方块

        // 使用方块 ID 创建一个标准的 BlockState
        BlockState startState = block.getDefaultState();

        queue.add(startPos);
        visited.add(startPos);

        int connectedCount = 0; // 用来记录已找到的相同方块数

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);
            connectedCount++; // 每找到一个相同方块，计数器加1

            // 如果查找到的相同方块超过MAX_CONNECTED_BLOCKS个，跳出并进行立方体查找
            if (connectedCount > MAX_CONNECTED_BLOCKS) {
                if(config.useRadiusSearchWhenReachBFSLimit){
                // 调用立方体查找方法，返回结果
                return findBlocksInCube(world, startPos, startBlockID);
                }else{
                    return null;
                }
            }

            // 遍历 26 个方向
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
    /** 生成 26 个方向的偏移量 */
    private static List<BlockPos> getAllNeighborOffsets() {
        List<BlockPos> offsets = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (!(x == 0 && y == 0 && z == 0)) { // 排除自身
                        offsets.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return offsets;
    }

    private static boolean isSameBlock(World world, BlockState startState, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == startState.getBlock();
    }
}

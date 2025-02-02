package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.vein_mine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utils {
    public static int searchRadius = vein_mine.config.searchRadius; // 搜索半径，1表示上下左右斜对角的8个方块，再加上中心方块
    public static int bfsLimit = vein_mine.config.BFSLimit;// 连锁搜索最大数量 超过就使用普通搜索

    // 创建一个Map来存储每个玩家的开关状态
    private static final Map<UUID, Boolean> playerVeinMineSwitchState = new HashMap<>();

    // 切换特定玩家的开关状态
    public static boolean toggleVeinMineSwitchState(PlayerEntity player) {
        UUID playerId = player.getUuid();
        boolean newState = !playerVeinMineSwitchState.getOrDefault(playerId, false);
        playerVeinMineSwitchState.put(playerId, newState);
        return newState;
    }

    // 获取特定玩家的当前开关状态
    public static boolean getVeinMineSwitchState(PlayerEntity player) {
        return playerVeinMineSwitchState.getOrDefault(player.getUuid(), false);
    }

    public static boolean isToolSuitable(BlockState blockState, PlayerEntity player){
        ItemStack tool = player.getMainHandStack();
        if (blockState.isToolRequired()){
            return tool.isSuitableFor(blockState);
        }
        return true;
    }

    public static void applyToolDurabilityDamage(PlayerEntity player, int blockCount) {
        // 获取玩家主手工具
        ItemStack tool = player.getMainHandStack();

        // 检查工具是否能被损耗
        if (tool.isDamageable()) {
            // 按照破坏的方块数量扣除耐久
            tool.damage(blockCount, player, null);
        }
    }

    public static boolean isSilktouch(PlayerEntity player) {
        ItemStack tool = player.getMainHandStack();
        // 检查工具是否具有精准采集的附魔
        return tool.getEnchantments().toString().contains("minecraft:silk_touch");
    }

    //这是没有凋落物的方块 不要让他掉东西
    public static boolean shouldNotDropItem(BlockState state, World world, BlockPos pos){ // 判断方块是否应该掉落物品
        state.getBlock();
        return Block.getDroppedStacks(state, (ServerWorld) world, pos, null).isEmpty();
    }

    public static boolean isContainer(BlockState state) {
        return state.hasBlockEntity();
    }
}

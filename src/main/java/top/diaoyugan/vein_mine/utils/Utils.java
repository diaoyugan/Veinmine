package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utils {
    // 连锁开关状态
    private static boolean veinMineSwitchState = false; // 初始为关
    // 切换开关状态的方法
    public static boolean toggleVeinMineSwitchState() {
        veinMineSwitchState = !veinMineSwitchState;
        return veinMineSwitchState;
    }


    // 获取当前开关状态
    public static boolean getVeinMineSwitchState() {
        return veinMineSwitchState;
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

    public static boolean shouldBreakWithoutDrop(BlockState targetState, PlayerEntity player, World world, BlockPos targetPos) {
        return player.isInCreativeMode() ||
                !Utils.isToolSuitable(targetState, player) ||
                shouldDropItem(targetState, world, targetPos);
    }

    public static boolean isSilktouch(PlayerEntity player) {
        ItemStack tool = player.getMainHandStack();
        // 检查工具是否具有精准采集的附魔
        return tool.getEnchantments().toString().contains("minecraft:silk_touch");
    }

    public static boolean shouldDropItem(BlockState state, World world, BlockPos pos){ // 判断方块是否应该掉落物品
        state.getBlock();
        return Block.getDroppedStacks(state, (ServerWorld) world, pos, null).isEmpty();
    }

    public static boolean isContainer(BlockState state) {
        return state.hasBlockEntity();
    }
}

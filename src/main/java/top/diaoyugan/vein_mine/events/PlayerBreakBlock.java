package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.keybindreciever.NetworkingKeybindPacket;
import top.diaoyugan.vein_mine.utils.Logger;

import static top.diaoyugan.vein_mine.utils.Utils.*;

public class PlayerBreakBlock {

    public static void register() {
        // 注册事件
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, entity) -> veinmine(world, player, pos, state)));
    }

    private static void veinmine(World world, PlayerEntity player, BlockPos pos, BlockState state){

        int radius = 1;  // 设置搜索半径，1表示上下左右斜对角的8个方块，再加上中心方块
        int destroyedCount = 0;
        if (NetworkingKeybindPacket.getSwitchState()) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos targetPos = pos.add(x, y, z);
                        if (targetPos.equals(pos)) continue; // 排除中心方块

                        BlockState targetState = world.getBlockState(targetPos);
                        Block targetBlock = targetState.getBlock();
                        if (targetBlock != state.getBlock()) continue;

                        if (shoudBreakWithoutDrop(targetState, player, world, targetPos)) {
                            world.breakBlock(targetPos, false);
                        } else if (isContainer(targetState)) {
                            world.breakBlock(targetPos, true);
                        } else if (isSilktouch(player)) {
                            world.breakBlock(targetPos, false);
                            Block.dropStack(world, targetPos, new ItemStack(targetBlock));
                        } else {
                            world.breakBlock(targetPos, true);
                        }

                        destroyedCount++;
                    }
                }
            }

            // 处理工具耐久
            if (!player.isInCreativeMode() && destroyedCount > 0) {
                applyToolDurabilityDamage(player, destroyedCount);
            }
        }

        Logger.newLogBlockBroken(state, pos, world, destroyedCount);
    }
}

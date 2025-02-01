package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.utils.Logger;
import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.List;

import static top.diaoyugan.vein_mine.utils.Utils.*;

public class PlayerBreakBlock {

    public static void register() {
        // 注册事件
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, entity) -> veinmine(world, player, pos, state)));
    }

    private static void veinmine(World world, PlayerEntity player, BlockPos pos, BlockState state){

        int destroyedCount = 0;
        if (Utils.getVeinMineSwitchState()) {
            Identifier startBlockID = Registries.BLOCK.getId(state.getBlock());
            List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos,startBlockID);
            for (BlockPos targetPos : blocksToBreak) {
                if (targetPos.equals(pos)) continue; // 排除中心方块

                BlockState targetState = world.getBlockState(targetPos);
                Block targetBlock = targetState.getBlock();
                if (targetBlock != state.getBlock()) continue;

                if (shouldBreakWithoutDrop(targetState, player, world, targetPos)) {
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

            // 处理工具耐久
            if (!player.isInCreativeMode() && destroyedCount > 0) {
                applyToolDurabilityDamage(player, destroyedCount);
            }

        Logger.newLogBlockBroken(state, pos, world, destroyedCount);
    }
}

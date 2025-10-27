package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class UtilsOverride {
    public static int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state) {
        int cost = 0;
        for (BlockPos targetPos : blocksToBreak) {
            BlockState targetState = player.getWorld().getBlockState(targetPos);
            if (Utils.isToolSuitable(targetState, player)) {
                cost++;
            }
        }
        return cost;
    }
}

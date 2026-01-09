package top.diaoyugan.vein_mine.utils;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class UtilsOverride {
    public static int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, Player player, BlockState state) {
        int cost = 0;
        for (BlockPos targetPos : blocksToBreak) {
            BlockState targetState = player.level().getBlockState(targetPos);
            if (Utils.isToolSuitable(targetState, player)) {
                cost++;
            }
        }
        return cost;
    }
}

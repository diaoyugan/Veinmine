package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ServerVersionInterface {
    void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar);
    int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state);
    void damageToolForVein(PlayerEntity player, ItemStack tool, int amount, Hand hand);
}

package top.diaoyugan.vein_mine.utils;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.networking.HighlightBlock;

import java.util.List;

public interface ServerVersionInterface {
    void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar);
    int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state);
    void PacketReceive(HighlightBlock.BlockHighlightPayloadC2S payload, ServerPlayNetworking.Context context);
}

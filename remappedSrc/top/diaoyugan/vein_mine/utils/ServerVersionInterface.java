package top.diaoyugan.vein_mine.utils;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import top.diaoyugan.vein_mine.networking.highlightingpacket.BlockHighlightRequest;


import java.util.List;

public interface ServerVersionInterface {
    void sendMessage(ServerPlayer player, Component message, Boolean isOnActionbar);
    int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, Player player, BlockState state);
    void PacketReceive(BlockHighlightRequest payload, ServerPlayNetworking.Context context);
}

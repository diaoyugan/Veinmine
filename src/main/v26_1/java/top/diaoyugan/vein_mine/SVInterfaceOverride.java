package top.diaoyugan.vein_mine;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import top.diaoyugan.vein_mine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.vein_mine.networking.HighlightBlockOverride;
import top.diaoyugan.vein_mine.utils.MessagesOverride;
import top.diaoyugan.vein_mine.utils.ServerVersionInterface;
import top.diaoyugan.vein_mine.utils.UtilsOverride;

import java.util.List;

public class SVInterfaceOverride implements ServerVersionInterface {
    @Override
    public void sendMessage(ServerPlayer player, Component message, Boolean isOnActionbar) {
        MessagesOverride.sendMessage(player,message,isOnActionbar);
    }

    @Override
    public int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, Player player, BlockState state) {
        return UtilsOverride.calculateTotalDurabilityCost(blocksToBreak, player, state);
    }

    @Override
    public void PacketReceive(BlockHighlightRequest payload, ServerPlayNetworking.Context context) {
        HighlightBlockOverride.receive(payload, context);
    }
}

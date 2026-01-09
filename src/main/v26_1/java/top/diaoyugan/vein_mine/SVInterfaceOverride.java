package top.diaoyugan.vein_mine;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.vein_mine.networking.HighlightBlockOverride;
import top.diaoyugan.vein_mine.utils.MessagesOverride;
import top.diaoyugan.vein_mine.utils.ServerVersionInterface;
import top.diaoyugan.vein_mine.utils.UtilsOverride;

import java.util.List;

public class SVInterfaceOverride implements ServerVersionInterface {
    @Override
    public void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        MessagesOverride.sendMessage(player,message,isOnActionbar);
    }

    @Override
    public int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state) {
        return UtilsOverride.calculateTotalDurabilityCost(blocksToBreak, player, state);
    }

    @Override
    public void PacketReceive(BlockHighlightRequest payload, ServerPlayNetworking.Context context) {
        HighlightBlockOverride.receive(payload, context);
    }
}

package top.diaoyugan.vein_mine.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ServerNetBridgeImpl implements ServerNetBridge {

    @Override
    public void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkIds.KEY_PRESS,
                (server, player, handler, buf, responseSender) ->
                        server.execute(() -> NetHandlers.handleKeyPress(player)));

        ServerPlayNetworking.registerGlobalReceiver(NetworkIds.HIGHLIGHT_REQUEST,
                (server, player, handler, buf, responseSender) -> {
                    long packed = buf.readLong();
                    server.execute(() -> NetHandlers.handleHighlightRequest(player, BlockPos.fromLong(packed)));
                });
    }

    @Override
    public void sendKeyResponse(ServerPlayerEntity player, boolean state) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(state);
        ServerPlayNetworking.send(player, NetworkIds.KEY_RESPONSE, buf);
    }

    @Override
    public void sendHighlightResponse(ServerPlayerEntity player, List<BlockPos> positions) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(positions.size());
        for (BlockPos pos : positions) {
            buf.writeLong(pos.asLong());
        }
        ServerPlayNetworking.send(player, NetworkIds.HIGHLIGHT_RESPONSE, buf);
    }
}

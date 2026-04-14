package top.diaoyugan.vein_mine.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.client.ClientBlockHighlighting;
import top.diaoyugan.vein_mine.client.HotKeys;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientNetBridgeImpl implements ClientNetBridge {

    @Override
    public void registerReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkIds.KEY_RESPONSE,
                (client, handler, buf, responseSender) -> {
                    boolean state = buf.readBoolean();
                    client.execute(() -> HotKeys.receiveKeybindingResponse(state));
                });

        ClientPlayNetworking.registerGlobalReceiver(NetworkIds.HIGHLIGHT_RESPONSE,
                (client, handler, buf, responseSender) -> {
                    int n = buf.readInt();
                    List<BlockPos> positions = new ArrayList<>(n);
                    for (int i = 0; i < n; i++) {
                        positions.add(BlockPos.fromLong(buf.readLong()));
                    }
                    client.execute(() -> ClientBlockHighlighting.applyHighlightedBlocks(positions));
                });
    }

    @Override
    public void sendKeyPress() {
        ClientPlayNetworking.send(NetworkIds.KEY_PRESS, PacketByteBufs.empty());
    }

    @Override
    public void sendHighlightRequest(BlockPos pos) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(pos.asLong());
        ClientPlayNetworking.send(NetworkIds.HIGHLIGHT_REQUEST, buf);
    }
}

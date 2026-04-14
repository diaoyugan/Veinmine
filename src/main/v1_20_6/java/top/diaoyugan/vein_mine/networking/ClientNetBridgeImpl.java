package top.diaoyugan.vein_mine.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.client.ClientBlockHighlighting;
import top.diaoyugan.vein_mine.client.HotKeys;

@Environment(EnvType.CLIENT)
public class ClientNetBridgeImpl implements ClientNetBridge {

    @Override
    public void registerReceivers() {
        ServerNetBridgeImpl.ensurePayloadsRegistered();

        ClientPlayNetworking.registerGlobalReceiver(Payloads.KeyResponse.ID,
                (payload, context) -> {
                    boolean state = payload.state();
                    context.client().execute(() -> HotKeys.receiveKeybindingResponse(state));
                });

        ClientPlayNetworking.registerGlobalReceiver(Payloads.HighlightResponse.ID,
                (payload, context) -> {
                    java.util.List<BlockPos> positions = payload.positions();
                    context.client().execute(() -> ClientBlockHighlighting.applyHighlightedBlocks(positions));
                });
    }

    @Override
    public void sendKeyPress() {
        ClientPlayNetworking.send(Payloads.KeyPress.INSTANCE);
    }

    @Override
    public void sendHighlightRequest(BlockPos pos) {
        ClientPlayNetworking.send(new Payloads.HighlightRequest(pos));
    }
}

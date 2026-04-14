package top.diaoyugan.vein_mine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ServerNetBridgeImpl implements ServerNetBridge {

    private static boolean payloadsRegistered = false;

    @Override
    public void registerReceivers() {
        ensurePayloadsRegistered();

        ServerPlayNetworking.registerGlobalReceiver(Payloads.KeyPress.ID,
                (payload, context) ->
                        context.server().execute(() -> NetHandlers.handleKeyPress(context.player())));

        ServerPlayNetworking.registerGlobalReceiver(Payloads.HighlightRequest.ID,
                (payload, context) -> {
                    BlockPos pos = payload.pos();
                    context.server().execute(() -> NetHandlers.handleHighlightRequest(context.player(), pos));
                });
    }

    @Override
    public void sendKeyResponse(ServerPlayerEntity player, boolean state) {
        ServerPlayNetworking.send(player, new Payloads.KeyResponse(state));
    }

    @Override
    public void sendHighlightResponse(ServerPlayerEntity player, List<BlockPos> positions) {
        ServerPlayNetworking.send(player, new Payloads.HighlightResponse(new ArrayList<>(positions)));
    }

    static void ensurePayloadsRegistered() {
        if (payloadsRegistered) return;
        payloadsRegistered = true;
        PayloadTypeRegistry.playC2S().register(Payloads.KeyPress.ID, Payloads.KeyPress.CODEC);
        PayloadTypeRegistry.playS2C().register(Payloads.KeyResponse.ID, Payloads.KeyResponse.CODEC);
        PayloadTypeRegistry.playC2S().register(Payloads.HighlightRequest.ID, Payloads.HighlightRequest.CODEC);
        PayloadTypeRegistry.playS2C().register(Payloads.HighlightResponse.ID, Payloads.HighlightResponse.CODEC);
    }
}

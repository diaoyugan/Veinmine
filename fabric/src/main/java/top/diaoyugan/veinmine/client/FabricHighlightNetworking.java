package top.diaoyugan.veinmine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;

public final class FabricHighlightNetworking {

    private FabricHighlightNetworking() {}

    public static void onInitialize() {
        ClientPlayNetworking.registerGlobalReceiver(
                BlockHighlightResponse.ID,
                (payload, context) ->
                        ClientHighlightLogic.onHighlightResponse(payload.positions())
        );
    }
}

package top.diaoyugan.veinmine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightCallbacks;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;

public final class FabricHighlightNetworking {

    private FabricHighlightNetworking() {}

    public static void onInitialize() {
        ClientPlayNetworking.registerGlobalReceiver(
                BlockHighlightResponse.ID,
                (payload, context) ->
                        ClientHighlightCallbacks.onHighlightResponse(payload.positions())
        );
    }

    public static void tick(LocalPlayer player) {
        BlockPos pos = ClientHighlightLogic.getLookedBlock(player);
        if (pos != null) {
            ClientPlayNetworking.send(new BlockHighlightRequest(pos));
        } else {
            ClientHighlightCallbacks.onMissedTarget();
        }
    }
}

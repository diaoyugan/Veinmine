package top.diaoyugan.vein_mine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import top.diaoyugan.vein_mine.SVInterfaceOverride;
import top.diaoyugan.vein_mine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.vein_mine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.vein_mine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.vein_mine.networking.keypacket.KeyResponsePacket;
import top.diaoyugan.vein_mine.utils.ServerVersionInterface;

public final class NetPacketsRegistrar {
    private NetPacketsRegistrar() {}

    public static void init() {
        // Keybinding packets
        PayloadTypeRegistry.playC2S().register(KeyPressPacket.ID, KeyPressPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(KeyResponsePacket.ID, KeyResponsePacket.CODEC);

        // Block highlight packets
        PayloadTypeRegistry.playC2S().register(BlockHighlightRequest.ID, BlockHighlightRequest.CODEC);
        PayloadTypeRegistry.playS2C().register(BlockHighlightResponse.ID, BlockHighlightResponse.CODEC);

        // Register C2S receiver for BlockHighlightRequest when a player connection initializes.
        ServerPlayConnectionEvents.INIT.register((handler, server) ->
                ServerPlayNetworking.registerReceiver(handler, BlockHighlightRequest.ID, NetPacketsRegistrar::handleBlockHighlightRequest)
        );
    }

    /**
     * 将接收到的请求分发到原有的处理逻辑（保持原先 SVI.PacketReceive 调用）。
     */
    private static void handleBlockHighlightRequest(BlockHighlightRequest payload, ServerPlayNetworking.Context context) {
        // 调用原有的处理器
        ServerVersionInterface svi = new SVInterfaceOverride();
        svi.PacketReceive(payload, context);
    }
}
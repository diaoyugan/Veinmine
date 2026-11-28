package top.diaoyugan.veinmine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;

public final class NetPacketsRegistrar {
    private NetPacketsRegistrar() {}

    public static void init() {
        // Keybinding packets
        PayloadTypeRegistry.serverboundPlay().register(KeyPressPacket.ID, KeyPressPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(KeyResponsePacket.ID, KeyResponsePacket.CODEC);

        // Block highlight packets
        PayloadTypeRegistry.serverboundPlay().register(BlockHighlightRequest.ID, BlockHighlightRequest.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(BlockHighlightResponse.ID, BlockHighlightResponse.CODEC);

        PayloadTypeRegistry.serverboundPlay().register(MiningPreferencesPacket.ID, MiningPreferencesPacket.CODEC);

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, BlockHighlightRequest.ID, (payload, context) ->
                    ServerMiningNetworking.handleHighlightRequest(context.player(), payload,
                            response -> ServerPlayNetworking.send(context.player(), response)));
            ServerPlayNetworking.registerReceiver(handler, MiningPreferencesPacket.ID, (payload, context) ->
                    ServerMiningNetworking.handlePreferences(context.player(), payload));
        });
    }
}

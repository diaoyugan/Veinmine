package top.diaoyugan.veinmine.networking;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;

@EventBusSubscriber(modid = Constants.ID)
public final class NeoForgeMiningNetworking {
    private NeoForgeMiningNetworking() {}

    /** 注册客户端/服务端包 */
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");

        registrar.playToServer(
                BlockHighlightRequest.ID,
                BlockHighlightRequest.CODEC,
                (payload, context) -> ServerMiningNetworking.handleHighlightRequest(
                        (ServerPlayer) context.player(), payload, context::reply)
        );

        registrar.playToClient(
                BlockHighlightResponse.ID,
                BlockHighlightResponse.CODEC,
                (payload, context) -> ClientHighlightLogic.onHighlightResponse(payload.positions())
        );

        registrar.optional().playToServer(
                MiningPreferencesPacket.ID,
                MiningPreferencesPacket.CODEC,
                (payload, context) -> ServerMiningNetworking.handlePreferences(
                        (ServerPlayer) context.player(), payload)
        );
    }
}

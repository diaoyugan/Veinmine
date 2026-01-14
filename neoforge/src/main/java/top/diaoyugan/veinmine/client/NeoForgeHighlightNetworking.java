package top.diaoyugan.veinmine.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.registration.ClientNetworkRegistry;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightCallbacks;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Constants.ID)
public final class NeoForgeHighlightNetworking {

    private NeoForgeHighlightNetworking() {}

    /** 注册客户端/服务端包 */
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");

        // ===== C2S：客户端 -> 服务端 =====
        registrar.playToServer(
                BlockHighlightRequest.ID,
                BlockHighlightRequest.CODEC,
                (msg, ctx) -> {
                    var player = ctx.player();
                    var level = player.level();

                    List<BlockPos> result = new ArrayList<>();

                    if (Utils.getVeinMineSwitchState(player)) {
                        var blocks = SmartVein.findBlocks(level, msg.blockPos());
                        if (blocks != null) {
                            result.addAll(blocks);
                        }
                    }

                    // 回包给这个玩家（S2C）
                    ctx.reply(new BlockHighlightResponse(new ArrayList<>(result)));
                }
        );

        // ===== S2C：服务端 -> 客户端 =====
        registrar.playToClient(
                BlockHighlightResponse.ID,
                BlockHighlightResponse.CODEC,
                (msg, ctx) ->
                        ClientHighlightCallbacks.onHighlightResponse(msg.positions())
        );
    }
}
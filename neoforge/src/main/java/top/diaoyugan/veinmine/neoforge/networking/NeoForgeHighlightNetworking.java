package top.diaoyugan.veinmine.neoforge.networking;

import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.*;

@EventBusSubscriber(modid = Constants.ID)
public final class NeoForgeHighlightNetworking {
    private static final Map<UUID, BlockPos> LAST_POS = new HashMap<>();
    private static final Map<UUID, Boolean> LAST_PROCESSED_STATE = new HashMap<>();
    private NeoForgeHighlightNetworking() {}

    /** 娉ㄥ唽瀹㈡埛绔?鏈嶅姟绔寘 */
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");

        // ===== C2S锛氬鎴风 -> 鏈嶅姟绔?=====
        registrar.playToServer(
                BlockHighlightRequest.ID,
                BlockHighlightRequest.CODEC,
                (msg, ctx) -> {
                    var player = ctx.player();
                    var level = player.level();
                    UUID playerId = player.getUUID();

                    BlockPos lastPos = LAST_POS.get(playerId);
                    Boolean lastState = LAST_PROCESSED_STATE.get(playerId);
                    boolean currentState = Utils.getVeinMineSwitchState(player);

                    var pos = msg.blockPos();

                    if (lastState != null
                            && pos.equals(lastPos)
                            && currentState == lastState) {
                        return;
                    }

                    LAST_POS.put(playerId, pos);
                    LAST_PROCESSED_STATE.put(playerId, currentState);

                    ArrayList<BlockPos> result = new ArrayList<>();
                    var blocks = SmartVein.findBlocks(level, pos);
                    if (blocks != null) {
                        result.addAll(blocks);
                    }

                    // 鍥炲寘缁欒繖涓帺瀹讹紙S2C锛?
                    ctx.reply(new BlockHighlightResponse(result));
                }
        );

        // ===== S2C锛氭湇鍔＄ -> 瀹㈡埛绔?=====
        registrar.playToClient(
                BlockHighlightResponse.ID,
                BlockHighlightResponse.CODEC,
                (msg, ctx) ->
                        ClientHighlightLogic.onHighlightResponse(msg.positions())
        );
    }

    public static void clearLastStates(UUID playerId){
        LAST_PROCESSED_STATE.remove(playerId);
        LAST_POS.remove(playerId);
    }
}
package top.diaoyugan.veinmine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.config.ServerMiningRules;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            ServerPlayNetworking.registerReceiver(handler, BlockHighlightRequest.ID, NetPacketsRegistrar::handleBlockHighlightRequest);
            ServerPlayNetworking.registerReceiver(handler, MiningPreferencesPacket.ID, (payload, context) -> {
                UUID playerId = context.player().getUUID();
                if (ServerMiningRules.updatePreferences(playerId, payload.rules())) clearLastStates(playerId);
            });
        });
    }

    private static final Map<UUID, HighlightQuery> LAST_QUERY = new HashMap<>();

    private record HighlightQuery(BlockPos pos, ServerLevel world, boolean active, long configRevision) {}

    private static void handleBlockHighlightRequest(BlockHighlightRequest payload, ServerPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        ServerPlayer player = context.player();
        ServerLevel world = (ServerLevel) player.level();
        // Same reach check vanilla applies to block breaking; also keeps far positions from loading chunks
        if (!player.isWithinBlockInteractionRange(pos, 1.0)) return;

        UUID playerId = player.getUUID();

        boolean currentState = Utils.getVeinMineSwitchState(player);
        HighlightQuery query = new HighlightQuery(pos, world, currentState, Config.getInstance().revision());
        if (query.equals(LAST_QUERY.put(playerId, query))) return;

        if (currentState) {
            var rules = ServerMiningRules.forPlayer(player);
            List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos, world.getBlockState(pos), rules::ignoresBlock);
            ServerPlayNetworking.send(player, new BlockHighlightResponse(
                    blocksToBreak == null ? new ArrayList<>() : new ArrayList<>(blocksToBreak)));
        }
    }
    public static void clearLastStates(UUID playerId){
        LAST_QUERY.remove(playerId);
    }
}

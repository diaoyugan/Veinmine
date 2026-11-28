package top.diaoyugan.veinmine.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.config.ServerMiningRules;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class ServerMiningNetworking {
    private static final Map<UUID, HighlightQuery> LAST_QUERY = new HashMap<>();

    private ServerMiningNetworking() {}

    private record HighlightQuery(BlockPos pos, Level world, boolean active, long configRevision) {}

    public static void handlePreferences(ServerPlayer player, MiningPreferencesPacket payload) {
        UUID playerId = player.getUUID();
        if (ServerMiningRules.updatePreferences(playerId, payload.rules())) LAST_QUERY.remove(playerId);
    }

    public static void handleHighlightRequest(ServerPlayer player, BlockHighlightRequest payload,
                                              Consumer<BlockHighlightResponse> reply) {
        BlockPos pos = payload.blockPos();
        // Use the vanilla block breaking reach check to avoid loading distant chunks.
        if (!player.isWithinBlockInteractionRange(pos, 1.0)) return;

        Level world = player.level();
        boolean active = Utils.getVeinMineSwitchState(player);
        HighlightQuery query = new HighlightQuery(pos, world, active, Config.getInstance().revision());
        if (query.equals(LAST_QUERY.put(player.getUUID(), query)) || !active) return;

        var rules = ServerMiningRules.forPlayer(player);
        var blocks = SmartVein.findBlocks(world, pos, world.getBlockState(pos), rules::ignoresBlock);
        reply.accept(new BlockHighlightResponse(blocks == null ? new ArrayList<>() : new ArrayList<>(blocks)));
    }

    public static void onPlayerDisconnect(UUID playerId) {
        Utils.clearVeinMineState(playerId);
        ServerMiningRules.clearPreferences(playerId);
        LAST_QUERY.remove(playerId);
    }
}

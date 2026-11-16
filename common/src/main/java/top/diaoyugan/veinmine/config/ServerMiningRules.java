package top.diaoyugan.veinmine.config;

import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.config.ConfigItems.ServerRuleMode;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ServerMiningRules {
    private static final Map<UUID, MiningRules> PREFERENCES = new ConcurrentHashMap<>();

    private ServerMiningRules() {}

    public static boolean updatePreferences(UUID playerId, MiningRules rules) {
        return !rules.equals(PREFERENCES.put(playerId, rules));
    }

    public static void clearPreferences(UUID playerId) {
        PREFERENCES.remove(playerId);
    }

    public static Effective forPlayer(ServerPlayer player) {
        var settings = Config.getInstance().miningSettings();
        return resolve(settings.mode(), PREFERENCES.get(player.getUUID()), settings.rules());
    }

    static Effective resolve(ServerRuleMode mode, MiningRules client, MiningRules server) {
        if (client == null || mode == ServerRuleMode.OVERRIDE) return new Effective(server, null);
        return new Effective(client, mode == ServerRuleMode.APPEND ? server : null);
    }

    public record Effective(MiningRules primary, MiningRules appended) {
        public boolean ignoresBlock(String id) {
            return primary.ignoredBlocks().contains(id) || appended != null && appended.ignoredBlocks().contains(id);
        }

        public int requiredDurability(String toolId) {
            int primaryThreshold = primary.requiredDurability(toolId);
            return appended == null ? primaryThreshold : Math.max(primaryThreshold, appended.requiredDurability(toolId));
        }
    }
}

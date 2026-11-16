package top.diaoyugan.veinmine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.networking.MiningPreferencesPacket;

final class FabricMiningPreferences {
    private static ClientPacketListener sentConnection;
    private static long sentRevision;

    private FabricMiningPreferences() {}

    static void sync(Minecraft client) {
        ClientPacketListener connection = client.getConnection();
        if (connection == null) {
            sentConnection = null;
            return;
        }
        if (!ClientPlayNetworking.canSend(MiningPreferencesPacket.ID)) return;

        Config config = Config.getInstance();
        long revision = config.revision();
        if (connection == sentConnection && revision == sentRevision) return;

        ClientPlayNetworking.send(new MiningPreferencesPacket(config.miningRules()));
        sentConnection = connection;
        sentRevision = revision;
    }
}

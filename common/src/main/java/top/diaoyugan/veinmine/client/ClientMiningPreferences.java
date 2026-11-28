package top.diaoyugan.veinmine.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.networking.MiningPreferencesPacket;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ClientMiningPreferences {
    private static ClientPacketListener sentConnection;
    private static long sentRevision;

    private ClientMiningPreferences() {}

    public static void sync(Minecraft client, Predicate<ClientPacketListener> canSend,
                            Consumer<MiningPreferencesPacket> send) {
        ClientPacketListener connection = client.getConnection();
        if (connection == null) {
            sentConnection = null;
            return;
        }
        if (!canSend.test(connection)) return;

        Config config = Config.getInstance();
        long revision = config.revision();
        if (connection == sentConnection && revision == sentRevision) return;

        send.accept(new MiningPreferencesPacket(config.miningRules()));
        sentConnection = connection;
        sentRevision = revision;
    }
}

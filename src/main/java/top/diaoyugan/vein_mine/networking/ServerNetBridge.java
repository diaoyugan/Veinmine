package top.diaoyugan.vein_mine.networking;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Version-specific abstraction over Fabric's server-side networking API.
 *
 * 1.20.1/1.20.4 implement this with the legacy {@code PacketByteBuf} API.
 * 1.20.6 implements it with the {@code PayloadTypeRegistry} / {@code CustomPayload} API
 * because the legacy overloads were removed in the 1.20.5 Fabric networking rewrite.
 */
public interface ServerNetBridge {
    ServerNetBridge INSTANCE = new ServerNetBridgeImpl();

    /** Wires up receivers for key-press and highlight-request packets. */
    void registerReceivers();

    /** Sends the toggle state back to the client after a key-press is processed. */
    void sendKeyResponse(ServerPlayerEntity player, boolean state);

    /** Sends the list of highlighted positions back to the requesting client. */
    void sendHighlightResponse(ServerPlayerEntity player, List<BlockPos> positions);
}

package top.diaoyugan.vein_mine.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;

/**
 * Version-specific abstraction over Fabric's client-side networking API.
 *
 * 1.20.1/1.20.4 use the legacy {@code PacketByteBuf} API; 1.20.6 uses the new
 * {@code PayloadTypeRegistry} / {@code CustomPayload} API.
 */
@Environment(EnvType.CLIENT)
public interface ClientNetBridge {
    ClientNetBridge INSTANCE = new ClientNetBridgeImpl();

    /** Wires up receivers for key-response and highlight-response packets. */
    void registerReceivers();

    /** Sends a key-press packet to the server. */
    void sendKeyPress();

    /** Sends a highlight-request packet for the given looked-at block. */
    void sendHighlightRequest(BlockPos pos);
}

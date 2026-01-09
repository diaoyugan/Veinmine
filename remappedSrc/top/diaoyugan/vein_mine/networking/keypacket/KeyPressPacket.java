
package top.diaoyugan.vein_mine.networking.keypacket;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.diaoyugan.vein_mine.networking.Networking;

public final class KeyPressPacket implements CustomPacketPayload {
    public static final KeyPressPacket INSTANCE = new KeyPressPacket();
    public static final CustomPacketPayload.Type<KeyPressPacket> ID = new CustomPacketPayload.Type<>(Networking.id("keybinding_press"));
    public static final StreamCodec<RegistryFriendlyByteBuf, KeyPressPacket> CODEC = StreamCodec.unit(INSTANCE);

    private KeyPressPacket() {}

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

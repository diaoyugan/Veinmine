
package top.diaoyugan.veinmine.networking.keypacket;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.diaoyugan.veinmine.networking.Networking;

public final class KeyPressPacket implements CustomPacketPayload {
    public static final KeyPressPacket INSTANCE = new KeyPressPacket();
    public static final Type<KeyPressPacket> ID = new Type<>(Networking.id("keybinding_press"));
    public static final StreamCodec<RegistryFriendlyByteBuf, KeyPressPacket> CODEC = StreamCodec.unit(INSTANCE);

    private KeyPressPacket() {}

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

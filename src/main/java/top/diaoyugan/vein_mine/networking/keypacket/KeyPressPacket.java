
package top.diaoyugan.vein_mine.networking.keypacket;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import top.diaoyugan.vein_mine.networking.Networking;

public final class KeyPressPacket implements CustomPayload {
    public static final KeyPressPacket INSTANCE = new KeyPressPacket();
    public static final CustomPayload.Id<KeyPressPacket> ID = new CustomPayload.Id<>(Networking.id("keybinding_press"));
    public static final PacketCodec<RegistryByteBuf, KeyPressPacket> CODEC = PacketCodec.unit(INSTANCE);

    private KeyPressPacket() {}

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

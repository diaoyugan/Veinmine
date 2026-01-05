package top.diaoyugan.vein_mine.networking.keypacket;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import top.diaoyugan.vein_mine.networking.Networking;

public record KeyResponsePacket(boolean state) implements CustomPayload {
    public static final CustomPayload.Id<KeyResponsePacket> ID = new CustomPayload.Id<>(Networking.id("keybinding_response"));

    public static final PacketCodec<RegistryByteBuf, KeyResponsePacket> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, KeyResponsePacket packet) {
            buf.writeBoolean(packet.state);
        }

        @Override
        public KeyResponsePacket decode(RegistryByteBuf buf) {
            return new KeyResponsePacket(buf.readBoolean());
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

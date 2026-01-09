package top.diaoyugan.vein_mine.networking.keypacket;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.diaoyugan.vein_mine.networking.Networking;

public record KeyResponsePacket(boolean state) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<KeyResponsePacket> ID = new CustomPacketPayload.Type<>(Networking.id("keybinding_response"));

    public static final StreamCodec<RegistryFriendlyByteBuf, KeyResponsePacket> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, KeyResponsePacket packet) {
            buf.writeBoolean(packet.state);
        }

        @Override
        public KeyResponsePacket decode(RegistryFriendlyByteBuf buf) {
            return new KeyResponsePacket(buf.readBoolean());
        }
    };

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

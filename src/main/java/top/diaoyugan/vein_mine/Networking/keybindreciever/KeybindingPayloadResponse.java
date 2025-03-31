package top.diaoyugan.vein_mine.Networking.keybindreciever;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import top.diaoyugan.vein_mine.Networking.Networking;

public record KeybindingPayloadResponse(boolean state) implements CustomPayload {

    public static final Id<KeybindingPayloadResponse> ID = new Id<>(Networking.id("keybinding_response"));

    // 自定义PacketCodec
    public static final PacketCodec<RegistryByteBuf, KeybindingPayloadResponse> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, KeybindingPayloadResponse packet) {
            buf.writeBoolean(packet.state);  // 将状态写入缓冲区
        }

        @Override
        public KeybindingPayloadResponse decode(RegistryByteBuf buf) {
            return new KeybindingPayloadResponse(buf.readBoolean());  // 从缓冲区读取状态
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package top.diaoyugan.vein_mine.Networking.keybindreciever;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import top.diaoyugan.vein_mine.Networking.Networking;

public record KeybindPayloadResponse(boolean state) implements CustomPayload {

    public static final Id<KeybindPayloadResponse> ID = new Id<>(Networking.id("keybind_response"));

    // 自定义PacketCodec
    public static final PacketCodec<RegistryByteBuf, KeybindPayloadResponse> CODEC = new PacketCodec<RegistryByteBuf, KeybindPayloadResponse>() {
        @Override
        public void encode(RegistryByteBuf buf, KeybindPayloadResponse packet) {
            buf.writeBoolean(packet.state);  // 将状态写入缓冲区
        }

        @Override
        public KeybindPayloadResponse decode(RegistryByteBuf buf) {
            return new KeybindPayloadResponse(buf.readBoolean());  // 从缓冲区读取状态
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package top.diaoyugan.vein_mine.network.keybindreciever;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import top.diaoyugan.vein_mine.vein_mine;

public class KeybindPayload implements CustomPayload {
    public static final KeybindPayload INSTANCE = new KeybindPayload();
    public static final CustomPayload.Id<KeybindPayload> ID = new CustomPayload.Id<>(vein_mine.id("keybind_press_test"));
    public static final PacketCodec<RegistryByteBuf, KeybindPayload> CODEC = PacketCodec.unit(INSTANCE);

    private KeybindPayload() { }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

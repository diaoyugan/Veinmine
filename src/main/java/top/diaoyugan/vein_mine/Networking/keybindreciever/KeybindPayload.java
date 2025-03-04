
package top.diaoyugan.vein_mine.Networking.keybindreciever;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import top.diaoyugan.vein_mine.Networking.Networking;

public class KeybindPayload implements CustomPayload {
	public static final KeybindPayload INSTANCE = new KeybindPayload();
	public static final Id<KeybindPayload> ID = new Id<>(Networking.id("keybind_press"));
	public static final PacketCodec<RegistryByteBuf, KeybindPayload> CODEC = PacketCodec.unit(INSTANCE);

	private KeybindPayload() { }

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}

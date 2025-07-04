
package top.diaoyugan.vein_mine.networking.keybindreciever;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import top.diaoyugan.vein_mine.networking.Networking;

public class KeybindingPayload implements CustomPayload {
	public static final KeybindingPayload INSTANCE = new KeybindingPayload();
	public static final Id<KeybindingPayload> ID = new Id<>(Networking.id("keybinding_press"));
	public static final PacketCodec<RegistryByteBuf, KeybindingPayload> CODEC = PacketCodec.unit(INSTANCE);

	private KeybindingPayload() { }

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}

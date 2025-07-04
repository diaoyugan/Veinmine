package top.diaoyugan.vein_mine.networking;

import net.minecraft.util.Identifier;

import static top.diaoyugan.vein_mine.vein_mine.ID;

public final class Networking {
	public static Identifier id(String name) {
		return Identifier.of(ID, name);
	}

	private Networking() {
	}
}

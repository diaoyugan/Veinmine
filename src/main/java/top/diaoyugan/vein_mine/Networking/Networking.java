package top.diaoyugan.vein_mine.Networking;

import net.minecraft.util.Identifier;

public final class Networking {
	public static final String ID = "vein_mine";
	public static Identifier id(String name) {
		return Identifier.of(ID, name);
	}

	private Networking() {
	}
}

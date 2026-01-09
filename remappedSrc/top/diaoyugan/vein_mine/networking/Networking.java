package top.diaoyugan.vein_mine.networking;

import static top.diaoyugan.vein_mine.vein_mine.ID;

import net.minecraft.resources.Identifier;

public final class Networking {
    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(ID, name);
    }

    private Networking() {
    }
}

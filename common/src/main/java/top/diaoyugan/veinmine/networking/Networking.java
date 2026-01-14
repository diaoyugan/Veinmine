package top.diaoyugan.veinmine.networking;

import net.minecraft.resources.Identifier;

import static top.diaoyugan.veinmine.Constants.ID;

public final class Networking {
    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(ID, name);
    }

    private Networking() {
    }
}

package top.diaoyugan.vein_mine.networking;

import net.minecraft.util.Identifier;

public final class NetworkIds {
    private NetworkIds() {}

    public static final Identifier KEY_PRESS = new Identifier("vein_mine", "keybinding_press");
    public static final Identifier KEY_RESPONSE = new Identifier("vein_mine", "keybinding_response");
    public static final Identifier HIGHLIGHT_REQUEST = new Identifier("vein_mine", "block_highlight");
    public static final Identifier HIGHLIGHT_RESPONSE = new Identifier("vein_mine", "block_highlight_response");
}

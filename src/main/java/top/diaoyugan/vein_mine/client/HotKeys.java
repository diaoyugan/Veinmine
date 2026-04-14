package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import top.diaoyugan.vein_mine.networking.ClientNetBridge;
import top.diaoyugan.vein_mine.utils.Utils;

public class HotKeys {
    private static boolean veinMineSwitchState = false;
    private static boolean lastPressed = false;

    public static void receiveKeybindingResponse(boolean newState) {
        veinMineSwitchState = newState;
    }

    @Environment(EnvType.CLIENT)
    public static boolean getVeinMineSwitchState() {
        return veinMineSwitchState;
    }

    public static void tickEvent(MinecraftClient client) {
        ClientVersionInterface CLI = new CLInterfaceOverride();
        if (client.getNetworkHandler() == null || client.player == null) return;

        boolean isPressed = CLI.KeyIsPressed();

        if (Utils.getConfig().useHoldInsteadOfToggle) {
            if (isPressed != lastPressed) {
                lastPressed = isPressed;
                ClientNetBridge.INSTANCE.sendKeyPress();
            }
        } else {
            if (CLI.KeyWasPressed()) {
                ClientNetBridge.INSTANCE.sendKeyPress();
            }
        }

        if (veinMineSwitchState) {
            ClientBlockHighlighting.checkPlayerLooking(client.player);
        } else {
            ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.clear();
        }
    }
}

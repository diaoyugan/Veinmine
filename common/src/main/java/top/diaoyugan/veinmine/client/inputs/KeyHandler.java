package top.diaoyugan.veinmine.client.inputs;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import top.diaoyugan.veinmine.client.configScreen.ConfigScreen;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.Set;

public final class KeyHandler {

    private static boolean wasPressed;

    private KeyHandler() {}

    public static void clientTick(Minecraft minecraft) {
        Window window = minecraft.getWindow();

        Set<Integer> keys = Utils.getConfig().configScreenKey;

        boolean pressed = true;

        for (int key : keys) {
            if (!InputConstants.isKeyDown(window, key)) {
                pressed = false;
                break;
            }
        }

        if (pressed && !wasPressed && !(minecraft.gui.screen() instanceof ConfigScreen)) {
            minecraft.setScreenAndShow(
                    new ConfigScreen(minecraft.gui.screen())
            );
        }

        wasPressed = pressed;
    }
}

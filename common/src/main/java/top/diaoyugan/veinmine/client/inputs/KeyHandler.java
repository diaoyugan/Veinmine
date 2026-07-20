package top.diaoyugan.veinmine.client.inputs;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.lwjgl.sdl.SDLMouse;
import top.diaoyugan.veinmine.client.configScreen.ConfigScreen;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.Collection;

public final class KeyHandler {

    private static boolean wasPressed;

    private KeyHandler() {}

    public static void clientTick(Minecraft minecraft) {
        boolean pressed = isCombinationDown(Utils.getConfig().configScreenKey);

        if (pressed
                && !wasPressed
                && !(minecraft.gui.screen() instanceof ConfigScreen)
                && !(minecraft.gui.screen() instanceof KeyBindsScreen)) {
            minecraft.setScreenAndShow(new ConfigScreen(minecraft.gui.screen()));
        }

        wasPressed = pressed;
    }

    private static boolean isCombinationDown(Collection<String> serializedKeys) {
        if (serializedKeys == null || serializedKeys.isEmpty()) {
            return false;
        }

        for (String serializedKey : serializedKeys) {
            InputConstants.Key key = InputConstants.getKey(serializedKey);
            if (key.equals(InputConstants.UNKNOWN) || !isDown(key)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDown(InputConstants.Key key) {
        if (key.getType() == InputConstants.Type.KEYBOARD) {
            return InputConstants.isKeyDown(key.getValue());
        }

        // SDL numbers mouse buttons from 1; its state uses bit (button - 1).
        int button = key.getValue();
        return button > 0
                && (SDLMouse.SDL_GetMouseState(null, null) & (1 << (button - 1))) != 0;
    }
}

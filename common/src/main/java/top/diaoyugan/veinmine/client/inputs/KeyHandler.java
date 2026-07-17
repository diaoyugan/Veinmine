package top.diaoyugan.veinmine.client.inputs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import top.diaoyugan.enchanted_ui.api.client.input.CombinationKeyBinding;
import top.diaoyugan.veinmine.client.configScreen.ConfigScreen;
import top.diaoyugan.veinmine.utils.Utils;

public final class KeyHandler {

    private static boolean wasPressed;

    private KeyHandler() {}

    public static void clientTick(Minecraft minecraft) {
        boolean pressed = CombinationKeyBinding.deserialize(Utils.getConfig().configScreenKey).isDown();

        if (pressed &&
                !wasPressed &&
                !(minecraft.gui.screen() instanceof ConfigScreen)&&
                !(minecraft.gui.screen() instanceof KeyBindsScreen)
        ) {
            minecraft.setScreenAndShow(
                    new ConfigScreen(minecraft.gui.screen())
            );
        }

        wasPressed = pressed;
    }
}

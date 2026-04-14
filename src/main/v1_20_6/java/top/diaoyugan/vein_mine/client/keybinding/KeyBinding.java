package top.diaoyugan.vein_mine.client.keybinding;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.utils.Utils;

public final class KeyBinding {
    public static net.minecraft.client.option.KeyBinding INSTANCE;

    private KeyBinding() {}

    public static void onInitialize() {
        int keyCode = Utils.getConfig().keyBindingCode;
        if (keyCode == 0) keyCode = GLFW.GLFW_KEY_GRAVE_ACCENT;

        INSTANCE = KeyBindingHelper.registerKeyBinding(
                new net.minecraft.client.option.KeyBinding(
                        "key.vm.switch",
                        InputUtil.Type.KEYSYM,
                        keyCode,
                        "category.vein_mine"));
    }

    public static void updateKeyCode(int keyCode) {
        if (INSTANCE != null) {
            INSTANCE.setBoundKey(InputUtil.fromKeyCode(keyCode, 0));
        }
    }
}

package top.diaoyugan.vein_mine.client.keybinding;

import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyCodes {
    public static InputUtil.Key getDefaultKey(){
        return InputUtil.fromKeyCode(GLFW.GLFW_KEY_GRAVE_ACCENT, 0);
    }
    public static InputUtil.Key getConfigKey(int keyCode){
        return InputUtil.fromKeyCode(keyCode, 0);
    }
}

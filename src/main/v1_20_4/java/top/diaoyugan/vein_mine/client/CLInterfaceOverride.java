package top.diaoyugan.vein_mine.client;

import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.client.keybinding.KeyBinding;
import top.diaoyugan.vein_mine.client.render.OutlineRenderer;

public class CLInterfaceOverride implements ClientVersionInterface {

    @Override
    public void OnInitialize() {
        KeyBinding.onInitialize();
        OutlineRenderer.register();
    }

    @Override
    public boolean KeyIsPressed() {
        return KeyBinding.INSTANCE != null && KeyBinding.INSTANCE.isPressed();
    }

    @Override
    public boolean KeyWasPressed() {
        return KeyBinding.INSTANCE != null && KeyBinding.INSTANCE.wasPressed();
    }

    @Override
    public net.minecraft.client.option.KeyBinding getBinding() {
        return KeyBinding.INSTANCE;
    }

    @Override
    public void UpDateKeyBinding(int code) {
        KeyBinding.updateKeyCode(code);
    }

    @Override
    public InputUtil.Key getDefaultKey() {
        return InputUtil.fromKeyCode(GLFW.GLFW_KEY_GRAVE_ACCENT, 0);
    }

    @Override
    public InputUtil.Key getConfigKey(int keyCode) {
        return InputUtil.fromKeyCode(keyCode, 0);
    }
}

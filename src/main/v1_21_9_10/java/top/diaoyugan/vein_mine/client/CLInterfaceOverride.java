
package top.diaoyugan.vein_mine.client;

import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.client.keybinding.KeyBinding;

public class CLInterfaceOverride implements ClientVersionInterface {
    @Override
    public void OnInitialize() {
        KeyBinding.onInitialize();
    }

    @Override
    public boolean KeyIsPressed() {
        return KeyBinding.BINDING.isPressed();
    }

    @Override
    public boolean KeyWasPressed() {
        return KeyBinding.BINDING.wasPressed();
    }

    @Override
    public net.minecraft.client.option.KeyBinding getBinding() {
        return KeyBinding.BINDING;
    }

    @Override
    public void UpDateKeyBinding(int code) {
        KeyBinding.updateKeyBinding(code);
    }

    @Override
    public InputUtil.Key getDefaultKey() {
        // 反引号键（`）
        return InputUtil.fromKeyCode(new KeyInput(GLFW.GLFW_KEY_GRAVE_ACCENT, 0, 0));
    }

    @Override
    public InputUtil.Key getConfigKey(int keyCode) {
        // 旧版逻辑：InputUtil.fromKeyCode(keyCode, 0)
        // 新版需要封装成 KeyInput 再传入
        return InputUtil.fromKeyCode(new KeyInput(keyCode, 0, 0));
    }
}


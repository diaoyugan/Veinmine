
package top.diaoyugan.vein_mine.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.client.config.ConfigOverride;
import top.diaoyugan.vein_mine.client.keybinding.KeyBinding;
import top.diaoyugan.vein_mine.client.render.RenderOutlines;
import top.diaoyugan.vein_mine.config.ConfigItems;

public class CLInterfaceOverride implements ClientVersionInterface {
    @Override
    public void OnInitialize() {
        KeyBinding.onInitialize();
        RenderOutlines.onInitialize();
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
        return InputUtil.fromKeyCode(GLFW.GLFW_KEY_GRAVE_ACCENT, 0);
    }

    @Override
    public InputUtil.Key getConfigKey(int keyCode) {
        return InputUtil.fromKeyCode(keyCode, 0);
    }
}


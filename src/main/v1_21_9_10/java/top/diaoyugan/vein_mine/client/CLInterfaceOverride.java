
package top.diaoyugan.vein_mine.client;

import net.minecraft.client.util.InputUtil;
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
        return KeyBinding.getDefaultKey(); // 从 KeyProvider 拿
    }

    @Override
    public InputUtil.Key getConfigKey(int keyCode) {
        return KeyBinding.getConfigKey(keyCode); // 从 KeyProvider 拿
    }
}



package top.diaoyugan.vein_mine.client;

import com.mojang.blaze3d.platform.InputConstants;
import top.diaoyugan.vein_mine.client.keybinding.KeyBinding;
import top.diaoyugan.vein_mine.client.render.RenderOutlines;

public class CLInterfaceOverride implements ClientVersionInterface {
    @Override
    public void OnInitialize() {
        KeyBinding.onInitialize();
        RenderOutlines.init();
    }

    @Override
    public boolean KeyIsPressed() {
        return KeyBinding.BINDING.isDown();
    }

    @Override
    public boolean KeyWasPressed() {
        return KeyBinding.BINDING.consumeClick();
    }

    @Override
    public net.minecraft.client.KeyMapping getBinding() {
        return KeyBinding.BINDING;
    }

    @Override
    public void UpDateKeyBinding(int code) {
        KeyBinding.updateKeyBinding(code);
    }

    @Override
    public InputConstants.Key getDefaultKey() {
        return KeyBinding.getDefaultKey(); // 从 KeyProvider 拿
    }

    @Override
    public InputConstants.Key getConfigKey(int keyCode) {
        return KeyBinding.getConfigKey(keyCode); // 从 KeyProvider 拿
    }
}



package top.diaoyugan.vein_mine.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.util.InputUtil;
import top.diaoyugan.vein_mine.client.config.ConfigOverride;
import top.diaoyugan.vein_mine.client.keybinding.KeyBinding;
import top.diaoyugan.vein_mine.client.render.RenderOutlines;
import top.diaoyugan.vein_mine.config.ConfigItems;

public class CLInterfaceOverride implements ClientVersionInterface {
    @Override
    public void OnInitialize() {
        KeyBinding.onInitialize();
        RenderOutlines.init();
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


    @Override
    public void createAdvanceConfig(ConfigBuilder cb, ConfigItems ci) {
        ConfigOverride.createAdvanceConfig(cb, ci);
    }
}


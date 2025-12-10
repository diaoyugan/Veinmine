package top.diaoyugan.vein_mine.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import top.diaoyugan.vein_mine.config.ConfigItems;

public interface ClientVersionInterface {
    void OnInitialize();
    boolean KeyIsPressed();
    boolean KeyWasPressed();
    net.minecraft.client.option.KeyBinding getBinding();
    void UpDateKeyBinding(int code);
    InputUtil.Key getDefaultKey();  // 默认键位
    InputUtil.Key getConfigKey(int keyCode);  // 从配置生成键位
}


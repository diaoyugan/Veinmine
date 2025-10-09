package top.diaoyugan.vein_mine.client;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public interface ClientVersionInterface {
    void OnInitialize();
    boolean KeyIsPressed();
    boolean KeyWasPressed();
    net.minecraft.client.option.KeyBinding getBinding();
    void UpDateKeyBinding(int code);
    InputUtil.Key getDefaultKey();  // 默认键位（例如 `GRAVE_ACCENT`）
    InputUtil.Key getConfigKey(int keyCode);  // 从配置生成键位
}


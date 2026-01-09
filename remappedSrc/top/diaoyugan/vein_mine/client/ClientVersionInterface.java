package top.diaoyugan.vein_mine.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import top.diaoyugan.vein_mine.config.ConfigItems;

public interface ClientVersionInterface {
    void OnInitialize();
    boolean KeyIsPressed();
    boolean KeyWasPressed();
    net.minecraft.client.KeyMapping getBinding();
    void UpDateKeyBinding(int code);
    InputConstants.Key getDefaultKey();  // 默认键位
    InputConstants.Key getConfigKey(int keyCode);  // 从配置生成键位
}


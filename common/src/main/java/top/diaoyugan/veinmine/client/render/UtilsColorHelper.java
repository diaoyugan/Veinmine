package top.diaoyugan.veinmine.client.render;

import top.diaoyugan.veinmine.config.ConfigItems;
import top.diaoyugan.veinmine.utils.Utils;

public final class UtilsColorHelper {

    private UtilsColorHelper() {}

    public static OutlineBuilder.Color fromConfig() {
        ConfigItems cfg = Utils.getConfig();
        return new OutlineBuilder.Color(
                cfg.red   / 255f,
                cfg.green / 255f,
                cfg.blue  / 255f,
                cfg.alpha / 255f
        );
    }
}
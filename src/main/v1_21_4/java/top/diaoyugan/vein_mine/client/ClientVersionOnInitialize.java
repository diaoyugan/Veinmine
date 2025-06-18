package top.diaoyugan.vein_mine.client;

import top.diaoyugan.vein_mine.client.render.RenderOutlines;

public class VersionInit implements ClientVersionInterface {
    @Override
    public void OnInitialize() {
        RenderOutlines.onInitialize();
    }
}

package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import top.diaoyugan.veinmine.config.ConfigItems;

public final class RenderProfiles {
    public static LineRenderProfile of(ConfigItems cfg) {
        boolean intrusive = cfg.useIntrusiveCode;

        return new LineRenderProfile(
                intrusive ? CustomRenderTypes.getLinesNoDepth()
                        : RenderTypes.lines(),
                intrusive ? OutlineBuilder.LineStyle.RIBBON_THICK_LINES
                        : OutlineBuilder.LineStyle.THIN_LINES
        );
    }
}
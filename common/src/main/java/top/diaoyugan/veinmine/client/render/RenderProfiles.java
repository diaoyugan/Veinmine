package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class RenderProfiles {
    public static LineRenderProfile of(Boolean intrusive) {
        return new LineRenderProfile(
                intrusive ? CustomRenderTypes.getLinesNoDepth()
                        : RenderTypes.lines(),
                intrusive ? LineStyles.RIBBON_THICK_LINES
                        : LineStyles.THIN_LINES
        );
    }
}
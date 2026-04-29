package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.renderer.rendertype.RenderType;

public record LineRenderProfile(
        RenderType type,
        OutlineBuilder.LineStyle style
) {}

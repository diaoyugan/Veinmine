package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.renderer.rendertype.RenderType;

public record LineRenderProfile(RenderType type, Style style) {
    public enum Style {
        THIN(1f),
        THICK(5f);

        final float width;

        Style(float width) {
            this.width = width;
        }
    }
}

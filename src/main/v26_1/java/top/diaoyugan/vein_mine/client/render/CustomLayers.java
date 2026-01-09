package top.diaoyugan.vein_mine.client.render;

import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import top.diaoyugan.vein_mine.mixin.RenderLayerInvoker;


public class CustomLayers {

    private static RenderType LINES_NO_DEPTH;

    public static void init() {
        if (LINES_NO_DEPTH != null) return;

        RenderSetup setup = RenderSetup.builder(CustomRenderPipeline.LINES_NO_DEPTH)
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .setOutputTarget(OutputTarget.MAIN_TARGET)
                .createRenderSetup();

        LINES_NO_DEPTH = RenderLayerInvoker.callOf("lines_no_depth", setup);
    }

    public static RenderType getLinesNoDepth() {
        if (LINES_NO_DEPTH == null) {
            throw new IllegalStateException("CustomLayers.init() not called!");
        }
        return LINES_NO_DEPTH;
    }
}

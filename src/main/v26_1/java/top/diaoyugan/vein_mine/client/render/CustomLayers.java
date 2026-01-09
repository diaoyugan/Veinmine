package top.diaoyugan.vein_mine.client.render;

import net.minecraft.client.render.LayeringTransform;
import net.minecraft.client.render.OutputTarget;
import top.diaoyugan.vein_mine.mixin.RenderLayerInvoker;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;


public class CustomLayers {

    private static RenderLayer LINES_NO_DEPTH;

    public static void init() {
        if (LINES_NO_DEPTH != null) return;

        RenderSetup setup = RenderSetup.builder(CustomRenderPipeline.LINES_NO_DEPTH)
                .layeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .outputTarget(OutputTarget.MAIN_TARGET)
                .build();

        LINES_NO_DEPTH = RenderLayerInvoker.callOf("lines_no_depth", setup);
    }

    public static RenderLayer getLinesNoDepth() {
        if (LINES_NO_DEPTH == null) {
            throw new IllegalStateException("CustomLayers.init() not called!");
        }
        return LINES_NO_DEPTH;
    }
}

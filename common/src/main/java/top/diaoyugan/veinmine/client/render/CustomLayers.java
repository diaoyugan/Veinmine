package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import top.diaoyugan.veinmine.mixin.RenderLayerInvoker;


public final class CustomLayers {

    private static RenderType LINES_NO_DEPTH;

    private CustomLayers() {}

    public static void init() {
        if (LINES_NO_DEPTH != null) return;

        RenderSetup setup = RenderSetup.builder(CustomRenderPipeline.LINES_NO_DEPTH)
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .setOutputTarget(OutputTarget.MAIN_TARGET)
                .createRenderSetup();

        LINES_NO_DEPTH = RenderLayerInvoker.callOf("lines_no_depth", setup);
    }

    /**
     * 惰性获取：保证任何调用点都不会炸
     */
    public static RenderType getLinesNoDepth() {
        if (LINES_NO_DEPTH == null) {
            init();
        }
        return LINES_NO_DEPTH;
    }
}

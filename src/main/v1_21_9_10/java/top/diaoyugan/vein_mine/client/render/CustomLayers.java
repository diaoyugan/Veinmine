package top.diaoyugan.vein_mine.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;

import java.util.OptionalDouble;

public class CustomLayers {
    private static RenderLayer.MultiPhase linesNoDepth;

    public static void init() {
        if (linesNoDepth != null) return;
        linesNoDepth = RenderLayer.of(
                "lines_no_depth",
                2048,
                false,
                false,
                CustomRenderPipeline.LINES_NO_DEPTH,
                RenderLayer.MultiPhaseParameters.builder()
                        .lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(2.0)))
                        .layering(RenderLayer.VIEW_OFFSET_Z_LAYERING)
                        .target(RenderLayer.ITEM_ENTITY_TARGET)
                        .build(false)
        );
    }


    public static RenderLayer.MultiPhase getLinesNoDepth() {
        if (linesNoDepth == null) {
            throw new IllegalStateException("CustomLayers not initialized!");
        }
        return linesNoDepth;
    }
}


package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;

import java.lang.reflect.Method;
import java.util.OptionalDouble;

public class CustomLayers {
    public static final RenderLayer.MultiPhase LINES_NO_DEPTH = createLinesNoDepth();

    private static RenderLayer.MultiPhase createLinesNoDepth() {
        try {
            // 找到原始私有方法 `RenderLayer.of(...)`
            Method method = RenderLayer.class.getDeclaredMethod(
                    "of",
                    String.class, int.class, boolean.class, boolean.class,
                    RenderPipeline.class, RenderLayer.MultiPhaseParameters.class
            );
            method.setAccessible(true);

            // 构造参数
            return (RenderLayer.MultiPhase) method.invoke(
                    null, // static 方法
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to create custom RenderLayer", e);
        }
    }
}







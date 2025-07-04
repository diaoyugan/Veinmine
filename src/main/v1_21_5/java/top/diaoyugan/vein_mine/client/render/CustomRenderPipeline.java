package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class CustomRenderPipeline {
    public static final RenderPipeline LINES_NO_DEPTH =
            RenderPipeline.builder()
                    .withLocation("pipeline/lines_no_depth")
                    .withVertexShader("core/position_color")
                    .withFragmentShader("core/position_color")
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST) // 禁用深度测试
                    .withDepthWrite(false) // 不写入深度缓冲
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINE_STRIP)
                    .build();
}

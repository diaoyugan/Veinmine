package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.mixin.RenderPipelinesInvoker;

public class CustomRenderPipeline {
    protected static final RenderPipeline LINES_NO_DEPTH = RenderPipelinesInvoker.invokeRegister(
            RenderPipeline.builder(RenderPipelinesInvoker.getLinesSnippet())
                    .withLocation(Identifier.fromNamespaceAndPath(Constants.ID, "pipeline/lines_no_depth"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
                    .withDepthWrite(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withCull(false)
                    .withColorWrite(true)
                    .build()
    );

}

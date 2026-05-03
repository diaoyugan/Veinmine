package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.mixin.RenderPipelinesInvoker;

public class CustomRenderPipeline {
    protected static final RenderPipeline LINES_NO_DEPTH = RenderPipelinesInvoker.invokeRegister(
            RenderPipeline.builder(RenderPipelinesInvoker.getLinesSnippet())
                    .withLocation(Identifier.fromNamespaceAndPath(Constants.ID, "pipeline/lines_no_depth"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH, VertexFormat.Mode.LINES)
                    .withDepthStencilState(ALWAYS_PASS())
                    .withColorTargetState(ColorTargetState.DEFAULT)
                    .withCull(false)
                    .build()
    );

    protected static DepthStencilState ALWAYS_PASS(){
        return new DepthStencilState(CompareOp.ALWAYS_PASS, false);
    }
}

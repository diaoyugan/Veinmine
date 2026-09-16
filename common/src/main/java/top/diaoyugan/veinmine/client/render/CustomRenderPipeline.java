package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.mixin.RenderPipelinesInvoker;

public class CustomRenderPipeline {
    protected static final RenderPipeline LINES_NO_DEPTH = RenderPipelinesInvoker.invokeRegister(
            RenderPipeline.builder(RenderPipelinesInvoker.getLinesSnippet())
                    .withLocation(Identifier.fromNamespaceAndPath(Constants.ID, "pipeline/lines_no_depth"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
                    .withPrimitiveTopology(PrimitiveTopology.LINES)
                    .withDepthStencilState(ALWAYS_PASS())
                    .withColorTargetState(ColorTargetState.DEFAULT)
                    .withCull(false)
                    .build()
    );

    protected static DepthStencilState ALWAYS_PASS(){
        return new DepthStencilState(CompareOp.ALWAYS_PASS, false);
    }
}

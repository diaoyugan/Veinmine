package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.pipeline.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.mixin.RenderTypeInvoker;

public final class RenderProfiles {
    private static final LineRenderProfile STANDARD =
            new LineRenderProfile(RenderTypes.lines(), LineRenderProfile.Style.THIN);

    private RenderProfiles() {}

    public static LineRenderProfile of(boolean seeThrough) {
        return seeThrough ? SeeThrough.PROFILE : STANDARD;
    }

    private static final class SeeThrough {
        private static final LineRenderProfile PROFILE = createProfile();

        private static LineRenderProfile createProfile() {
            // Fabric submits these lines after OIT, in a pass without a depth attachment.
            RenderPipeline pipeline = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(Constants.ID, "pipeline/lines_no_depth"))
                    .withBindGroupLayout(BindGroupLayouts.PROJECTION)
                    .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
                    .withBindGroupLayout(BindGroupLayouts.FOG)
                    .withVertexShader("core/rendertype_lines")
                    .withFragmentShader("core/rendertype_lines")
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
                    .withPrimitiveTopology(PrimitiveTopology.LINES)
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withCull(false)
                    .build();
            RenderSetup setup = RenderSetup.nonOitBuilder(pipeline)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup();
            return new LineRenderProfile(RenderTypeInvoker.callCreate("lines_no_depth", setup), LineRenderProfile.Style.THICK);
        }
    }
}

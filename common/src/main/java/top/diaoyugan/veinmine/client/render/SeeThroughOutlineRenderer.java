package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import top.diaoyugan.veinmine.Constants;

import java.util.List;

public final class SeeThroughOutlineRenderer extends RenderTypeFeatureRenderer<SeeThroughOutlineRenderer.Submit> {
    public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create(Constants.ID + ":see_through_outline");

    @Override
    protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
        for (Submit submit : submits) {
            VertexConsumer consumer = getVertexBuilder(submit.profile().type());
            for (BlockOutlineBuilder.Line line : submit.lines()) {
                LineRenderDispatcher.draw(consumer, submit.pose(), line, submit.profile().style());
            }
        }
    }

    public record Submit(PoseStack.Pose pose, LineRenderProfile profile, List<BlockOutlineBuilder.Line> lines)
            implements TranslucentSubmit {
        @Override
        public float distanceToCameraSq() {
            return TranslucentSubmit.computeDistanceToCameraSq(pose.pose());
        }

        @Override
        public FeatureRendererType<Submit> featureType() {
            return TYPE;
        }
    }
}

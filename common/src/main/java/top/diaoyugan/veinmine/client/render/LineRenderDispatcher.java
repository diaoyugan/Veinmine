package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class LineRenderDispatcher {
    private LineRenderDispatcher() {}

    public static void drawAll(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            List<BlockOutlineBuilder.Line> lines,
            LineRenderProfile.Style style
    ) {
        for (BlockOutlineBuilder.Line line : lines) {
            draw(consumer, pose, line, style);
        }
    }

    public static void draw(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            BlockOutlineBuilder.Line line,
            LineRenderProfile.Style style
    ) {
        Vec3 from = line.from();
        Vec3 to = line.to();
        Vec3 normal = to.subtract(from).normalize();
        BlockOutlineBuilder.Color color = line.color();

        consumer.addVertex(pose.pose(), (float) from.x, (float) from.y, (float) from.z)
                .setColor(color.r(), color.g(), color.b(), color.a())
                .setNormal((float) normal.x, (float) normal.y, (float) normal.z)
                .setLineWidth(style.width);

        consumer.addVertex(pose.pose(), (float) to.x, (float) to.y, (float) to.z)
                .setColor(color.r(), color.g(), color.b(), color.a())
                .setNormal((float) normal.x, (float) normal.y, (float) normal.z)
                .setLineWidth(style.width);
    }
}

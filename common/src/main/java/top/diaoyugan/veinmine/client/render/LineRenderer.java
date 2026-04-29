package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;

public final class LineRenderer {
    public static void draw(
            VertexConsumer c,
            PoseStack.Pose pose,
            Camera camera,
            OutlineBuilder.Line line,
            OutlineBuilder.LineStyle style
    ) {
        switch (style) {
            case THIN_LINES -> LineStyles.drawThin(c, pose, line);
            case RIBBON_THICK_LINES -> LineStyles.drawRibbon(c, pose, camera, line, 0.04f);
            case OLD_TRIANGLES -> LineStyles.drawQuadLine(c, pose, line, 0.02f);
        }
    }
}
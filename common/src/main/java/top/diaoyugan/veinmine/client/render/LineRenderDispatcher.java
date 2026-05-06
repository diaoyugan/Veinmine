package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class LineRenderDispatcher {
    public static void draw(
            VertexConsumer c,
            PoseStack.Pose pose,
            BlockOutlineBuilder.Line line,
            LineStyles style
    ) {
        switch (style) {
            case LineStyles.THIN_LINES -> LineRender.drawThin(c, pose, line);
            case LineStyles.RIBBON_THICK_LINES -> LineRender.drawThick(c, pose, line);
            case LineStyles.OLD_TRIANGLES -> LineRender.drawQuadLine(c, pose, line, 0.02f);
        }
    }
}

package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;

public class LineStyles {
    public static void drawThin(VertexConsumer c, PoseStack.Pose pose, OutlineBuilder.Line line) {
        var a = line.from();
        var b = line.to();
        var col = line.color();

        c.addVertex(pose.pose(), (float)a.x, (float)a.y, (float)a.z)
                .setColor(col.r(), col.g(), col.b(), col.a())
                .setNormal(0, 1, 0).setLineWidth(1f);

        c.addVertex(pose.pose(), (float)b.x, (float)b.y, (float)b.z)
                .setColor(col.r(), col.g(), col.b(), col.a())
                .setNormal(0, 1, 0).setLineWidth(1f);
    }
    public static void drawRibbon(
            VertexConsumer c,
            PoseStack.Pose pose,
            Camera camera,
            OutlineBuilder.Line line,
            float thickness
    ) {
        var p1 = line.from();
        var p2 = line.to();
        var col = line.color();

        Vec3 dir = p2.subtract(p1).normalize();

        // 关键：面向摄像机
        Vec3 view = camera.position().subtract(p1).normalize();

        Vec3 side = view.cross(dir);
        if (side.lengthSqr() < 1e-6) side = new Vec3(0, 1, 0);

        side = side.normalize().scale(thickness / 2);

        Vec3 v1 = p1.add(side);
        Vec3 v2 = p2.add(side);
        Vec3 v3 = p2.subtract(side);
        Vec3 v4 = p1.subtract(side);

        drawQuad(c, pose, v1, v2, v3, v4, col);
    }
    public static void drawQuadLine(
            VertexConsumer c,
            PoseStack.Pose pose,
            OutlineBuilder.Line line,
            float thickness
    ) {
        var p1 = line.from();
        var p2 = line.to();
        var col = line.color();

        Vec3 dir = p2.subtract(p1).normalize();

        Vec3 up = Math.abs(dir.y) > 0.99 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
        Vec3 side = dir.cross(up).normalize().scale(thickness / 2);

        Vec3 v1 = p1.add(side);
        Vec3 v2 = p2.add(side);
        Vec3 v3 = p2.subtract(side);
        Vec3 v4 = p1.subtract(side);

        drawQuad(c, pose, v1, v2, v3, v4, col);
    }

    private static void drawQuad(
            VertexConsumer c,
            PoseStack.Pose pose,
            Vec3 a, Vec3 b, Vec3 c1, Vec3 d,
            OutlineBuilder.Color col
    ) {
        // 两个三角形

        vertex(c, pose, a, col);
        vertex(c, pose, b, col);
        vertex(c, pose, c1, col);

        vertex(c, pose, a, col);
        vertex(c, pose, c1, col);
        vertex(c, pose, d, col);
    }

    private static void vertex(VertexConsumer c, PoseStack.Pose pose, Vec3 v, OutlineBuilder.Color col) {
        c.addVertex(pose.pose(), (float)v.x, (float)v.y, (float)v.z)
                .setColor(col.r(), col.g(), col.b(), col.a())
                .setNormal(0, 1, 0)
                .setLineWidth(1f);
    }
}

package top.diaoyugan.veinmine.client.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.Collection;

public final class OutlineRenderer {

    private OutlineRenderer() {}

    private static final int[][] EDGES = {
            {0,1},{1,2},{2,3},{3,0},
            {4,5},{5,6},{6,7},{7,4},
            {0,4},{1,5},{2,6},{3,7},
    };

    public enum LineStyle {
        THIN_LINES,
        RIBBON_THICK_LINES,
        OLD_TRIANGLES
    }

    public record Color(float r, float g, float b, float a) {}

    public static void render(
            PoseStack matrices,
            MultiBufferSource.BufferSource buffers,
            Camera camera,
            Collection<BlockPos> blocks,
            RenderType renderType,
            LineStyle style,
            Color color,
            boolean intrusive
    ) {
        if (blocks.isEmpty()) return;

        Vec3 camPos = camera.position();
        Matrix4f matrix = matrices.last().pose();

        matrices.pushPose();

        VertexConsumer consumer = buffers.getBuffer(renderType);

        for (BlockPos pos : blocks) {
            double x = pos.getX() - camPos.x;
            double y = pos.getY() - camPos.y;
            double z = pos.getZ() - camPos.z;

            Vec3[] corners = {
                    new Vec3(x, y, z),
                    new Vec3(x + 1, y, z),
                    new Vec3(x + 1, y + 1, z),
                    new Vec3(x, y + 1, z),
                    new Vec3(x, y, z + 1),
                    new Vec3(x + 1, y, z + 1),
                    new Vec3(x + 1, y + 1, z + 1),
                    new Vec3(x, y + 1, z + 1)
            };

            for (int[] edge : EDGES) {
                Vec3 p1 = corners[edge[0]];
                Vec3 p2 = corners[edge[1]];

                switch (style) {
                    case THIN_LINES ->
                            drawLineSimple(consumer, matrix, p1, p2, color);
                    case RIBBON_THICK_LINES -> {
                        if (intrusive)
                            drawBillboardRibbon(consumer, matrix, camera, p1, p2, color, 0.04f);
                        else
                            drawLineSimple(consumer, matrix, p1, p2, color);
                    }
                    case OLD_TRIANGLES ->
                            drawLineAsQuadOld(consumer, matrix, p1, p2, 0.02f, color);
                }
            }
        }

        buffers.endBatch();
        matrices.popPose();
    }

    /* ================= GL ================= */

    private static void prepareGlState() {
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._enableBlend();
    }

    private static void resetGlState() {
        GlStateManager._depthMask(true);
        GlStateManager._enableDepthTest();
        GlStateManager._disableBlend();
    }

    /* ================= Draw ================= */

    private static void drawLineSimple(VertexConsumer c, Matrix4f m, Vec3 p1, Vec3 p2, Color col) {
        c.addVertex(m, (float)p1.x, (float)p1.y, (float)p1.z)
                .setColor(col.r, col.g, col.b, col.a)
                .setNormal(0, 1, 0).setLineWidth(2);
        c.addVertex(m, (float)p2.x, (float)p2.y, (float)p2.z)
                .setColor(col.r, col.g, col.b, col.a)
                .setNormal(0, 1, 0).setLineWidth(2);
    }

    private static void drawBillboardRibbon(
            VertexConsumer c, Matrix4f m, Camera camera,
            Vec3 p1, Vec3 p2, Color col, float thickness
    ) {
        Vec3 dir = p2.subtract(p1).normalize();
        Vec3 camDir = camera.position().subtract(p1).normalize();
        Vec3 side = dir.cross(camDir);

        if (side.lengthSqr() < 1e-6) side = new Vec3(0, 1, 0);
        side = side.normalize().scale(thickness / 2);

        drawQuad(c, m,
                p1.add(side), p2.add(side),
                p2.subtract(side), p1.subtract(side),
                col
        );
    }

    private static void drawLineAsQuadOld(
            VertexConsumer c, Matrix4f m,
            Vec3 p1, Vec3 p2, float t, Color col
    ) {
        Vec3 dir = p2.subtract(p1).normalize();
        Vec3 up = Math.abs(dir.y) > 0.99 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
        Vec3 side = dir.cross(up).normalize().scale(t / 2);

        drawQuad(c, m,
                p1.add(side), p2.add(side),
                p2.subtract(side), p1.subtract(side),
                col
        );
    }

    private static void drawQuad(
            VertexConsumer c, Matrix4f m,
            Vec3 a, Vec3 b, Vec3 c1, Vec3 d,
            Color col
    ) {
        drawTriangle(c, m, a, b, c1, col);
        drawTriangle(c, m, c1, d, a, col);
    }

    private static void drawTriangle(
            VertexConsumer c, Matrix4f m,
            Vec3 v1, Vec3 v2, Vec3 v3, Color col
    ) {
        c.addVertex(m, (float)v1.x, (float)v1.y, (float)v1.z)
                .setColor(col.r, col.g, col.b, col.a).setNormal(0,1,0);
        c.addVertex(m, (float)v2.x, (float)v2.y, (float)v2.z)
                .setColor(col.r, col.g, col.b, col.a).setNormal(0,1,0);
        c.addVertex(m, (float)v3.x, (float)v3.y, (float)v3.z)
                .setColor(col.r, col.g, col.b, col.a).setNormal(0,1,0);
    }
}

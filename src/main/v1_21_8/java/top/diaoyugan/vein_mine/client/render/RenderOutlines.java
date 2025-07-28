package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import top.diaoyugan.vein_mine.client.ClientBlockHighlighting;
import top.diaoyugan.vein_mine.config.IntrusiveConfig;
import top.diaoyugan.vein_mine.utils.Utils;


public class RenderOutlines {
    protected static boolean initialized = false;
    public static void onInitialize() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (MinecraftClient.getInstance().world == null) return;

            if (!initialized) {
                initialized = true;
                CustomLayers.init();
            }

            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();

            MatrixStack matrices = context.matrixStack();
            VertexConsumerProvider vertexConsumers = context.consumers();

            GlStateManager._disableDepthTest();
            GlStateManager._depthMask(false);
            GlStateManager._enableBlend();

            if (matrices != null) {
                matrices.push();
            }

            RenderLayer layer = IntrusiveConfig.isEnabled()
                    ? CustomLayers.getLinesNoDepth()
                    : RenderLayer.getLineStrip();

            for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
                if (matrices != null) {
                    if (vertexConsumers != null) {
                        if (IntrusiveConfig.isEnabled()) {
                            drawOutlineBox(matrices, vertexConsumers.getBuffer(layer), pos, camPos);
                        } else {
                            drawOutlineBox(matrices, vertexConsumers.getBuffer(RenderLayer.getLineStrip()), pos, camPos);
                        }

                    }
                }
            }

            if (matrices != null) {
                matrices.pop();
            }

            GlStateManager._depthMask(true);
            GlStateManager._enableDepthTest();
            GlStateManager._disableBlend();
        });


    }

    private static void drawOutlineBox(MatrixStack matrices, VertexConsumer consumer, BlockPos pos, Vec3d cameraPos) {
        double x = pos.getX() - cameraPos.x;
        double y = pos.getY() - cameraPos.y;
        double z = pos.getZ() - cameraPos.z;

        float r = (float) Utils.getConfig().red / 255f;
        float g = (float) Utils.getConfig().green / 255f;
        float b = (float) Utils.getConfig().blue / 255f;
        float a = (float) Utils.getConfig().alpha / 255f;

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // 立方体的8个点
        Vec3d[] corners = new Vec3d[]{
                new Vec3d((float) x, (float) y, (float) z),
                new Vec3d((float) x + 1, (float) y, (float) z),
                new Vec3d((float) x + 1, (float) y + 1, (float) z),
                new Vec3d((float) x, (float) y + 1, (float) z),
                new Vec3d((float) x, (float) y, (float) z + 1),
                new Vec3d((float) x + 1, (float) y, (float) z + 1),
                new Vec3d((float) x + 1, (float) y + 1, (float) z + 1),
                new Vec3d((float) x, (float) y + 1, (float) z + 1),
        };

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7},
        };

        for (int[] edge : edges) {
            Vec3d p1 = corners[edge[0]];
            Vec3d p2 = corners[edge[1]];
//            consumer.vertex(matrix, (float) p1.getX(), (float) p1.getY(), (float) p1.getZ()).color(r, g, b, a).normal(0,1,0);
//            consumer.vertex(matrix, (float) p2.getX(), (float) p2.getY(), (float) p2.getZ()).color(r, g, b, a).normal(0,1,0);
            //drawThickLine(consumer, matrix, p1, p2, 0.01f , r, g, b, a);
            drawLineAsQuad(consumer, matrix, p1, p2, 0.01f, r, g, b, a);
        }
    }

    private static void drawLineAsQuad(VertexConsumer consumer, Matrix4f matrix, Vec3d p1, Vec3d p2, float thickness, float r, float g, float b, float a) {
        Vec3d dir = p2.subtract(p1).normalize();
        Vec3d up = new Vec3d(0, 1, 0);

        Vec3d side = dir.crossProduct(up).normalize().multiply(thickness / 2);
        if (side.lengthSquared() == 0) {
            side = new Vec3d(1, 0, 0).multiply(thickness / 2); // fallback for vertical lines
        }

        Vec3d p1a = p1.add(side);
        Vec3d p1b = p1.subtract(side);
        Vec3d p2a = p2.add(side);
        Vec3d p2b = p2.subtract(side);

        // 两个三角形组成一个矩形面
        drawTriangle(consumer, matrix, p1a, p2a, p2b, r, g, b, a);
        drawTriangle(consumer, matrix, p2b, p1b, p1a, r, g, b, a);
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f matrix, Vec3d v1, Vec3d v2, Vec3d v3, float r, float g, float b, float a) {
        consumer.vertex(matrix, (float) v1.x, (float) v1.y, (float) v1.z)
                .color(r, g, b, a).texture(0.0f, 0.0f).normal(0, 1, 0);
        consumer.vertex(matrix, (float) v2.x, (float) v2.y, (float) v2.z)
                .color(r, g, b, a).texture(0.0f, 0.0f).normal(0, 1, 0);
        consumer.vertex(matrix, (float) v3.x, (float) v3.y, (float) v3.z)
                .color(r, g, b, a).texture(0.0f, 0.0f).normal(0, 1, 0);
    }
}
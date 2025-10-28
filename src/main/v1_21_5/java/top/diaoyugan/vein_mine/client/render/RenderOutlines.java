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
import top.diaoyugan.vein_mine.config.ConfigItems;
import top.diaoyugan.vein_mine.config.IntrusiveConfig;
import top.diaoyugan.vein_mine.utils.Utils;


public class RenderOutlines {
    protected static boolean initialized = false;

    public static void onInitialize() {
        if(Utils.getConfig().enableHighlights) {
            WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.world == null) return;

                if (!initialized) {
                    initialized = true;
                    CustomLayers.init();
                }

                MatrixStack matrices = context.matrixStack();
                VertexConsumerProvider vertexConsumers = context.consumers();
                Vec3d camPos = context.camera().getPos();

                setupGlState();

                if (matrices != null && vertexConsumers != null) {
                    renderHighlightedBlocks(matrices, vertexConsumers, camPos);
                }

                restoreGlState();
            });
        }
    }

    private static void setupGlState() {
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._enableBlend();
    }

    private static void restoreGlState() {
        GlStateManager._depthMask(true);
        GlStateManager._enableDepthTest();
        GlStateManager._disableBlend();
    }

    private static void renderHighlightedBlocks(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d camPos) {
        RenderLayer defaultLayer = RenderLayer.getLineStrip();
        RenderLayer layer = IntrusiveConfig.isEnabled()
                ? CustomLayers.getLinesNoDepth()
                : defaultLayer;

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float[] color = getOutlineColor();

        for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
            VertexConsumer consumer = vertexConsumers.getBuffer(layer);
            drawOutlineBox(matrix, consumer, pos, camPos, color);
        }
    }

    private static float[] getOutlineColor() {
        ConfigItems config = Utils.getConfig();
        return new float[]{
                config.red / 255f,
                config.green / 255f,
                config.blue / 255f,
                config.alpha / 255f
        };
    }

    private static void drawOutlineBox(Matrix4f matrix, VertexConsumer consumer, BlockPos pos, Vec3d cameraPos, float[] color) {
        Vec3d camOffset = new Vec3d(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);
        Vec3d[] corners = getCubeCorners(camOffset);
        int[][] edges = getCubeEdges();

        for (int[] edge : edges) {
            drawLineAsQuad(consumer, matrix, corners[edge[0]], corners[edge[1]], 0.01f, color);
        }
    }

    private static Vec3d[] getCubeCorners(Vec3d offset) {
        double x = offset.x, y = offset.y, z = offset.z;
        return new Vec3d[]{
                new Vec3d(x, y, z), new Vec3d(x+1, y, z), new Vec3d(x+1, y+1, z), new Vec3d(x, y+1, z),
                new Vec3d(x, y, z+1), new Vec3d(x+1, y, z+1), new Vec3d(x+1, y+1, z+1), new Vec3d(x, y+1, z+1)
        };
    }

    private static int[][] getCubeEdges() {
        return new int[][]{
                {0, 1},{1, 2},{2, 3},{3, 0},
                {4, 5},{5, 6},{6, 7},{7, 4},
                {0, 4},{1, 5},{2, 6},{3, 7}
        };
    }

    private static void drawLineAsQuad(VertexConsumer consumer, Matrix4f matrix, Vec3d p1, Vec3d p2, float thickness, float[] color) {
        Vec3d dir = p2.subtract(p1).normalize();
        Vec3d side = dir.crossProduct(new Vec3d(0, 1, 0));
        if (side.lengthSquared() == 0) side = new Vec3d(1, 0, 0);
        side = side.normalize().multiply(thickness / 2);

        Vec3d p1a = p1.add(side), p1b = p1.subtract(side);
        Vec3d p2a = p2.add(side), p2b = p2.subtract(side);

        drawTriangle(consumer, matrix, p1a, p2a, p2b, color);
        drawTriangle(consumer, matrix, p2b, p1b, p1a, color);
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f matrix, Vec3d v1, Vec3d v2, Vec3d v3, float[] color) {
        consumer.vertex(matrix, (float)v1.x, (float)v1.y, (float)v1.z).color(color[0], color[1], color[2], color[3]).texture(0,0).normal(0,1,0);
        consumer.vertex(matrix, (float)v2.x, (float)v2.y, (float)v2.z).color(color[0], color[1], color[2], color[3]).texture(0,0).normal(0,1,0);
        consumer.vertex(matrix, (float)v3.x, (float)v3.y, (float)v3.z).color(color[0], color[1], color[2], color[3]).texture(0,0).normal(0,1,0);
    }
}

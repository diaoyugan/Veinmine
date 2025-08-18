package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
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

    private static final int[][] EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7},
    };

    // 记录类封装颜色
    private record Color(float r, float g, float b, float a) {}

    public static void onInitialize() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(RenderOutlines::renderHighlights);
    }

    private static void renderHighlights(WorldRenderContext context) {
        if (MinecraftClient.getInstance().world == null || ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.isEmpty()) return;

        if (!initialized) {
            initialized = true;
            CustomLayers.init(); // 初始化自定义渲染层
        }

        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider vertexConsumers = context.consumers();
        Vec3d camPos = context.camera().getPos();

        prepareGlState();
        matrices.push();

        RenderLayer layer = IntrusiveConfig.isEnabled() ? CustomLayers.getLinesNoDepth() : RenderLayer.getLineStrip();
        Color color = getConfigColor();

        // 绘制所有高亮方块的线框
        for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
            if (vertexConsumers != null) {
                VertexConsumer buffer = vertexConsumers.getBuffer(layer);
                drawOutlineBox(matrices, buffer, pos, camPos, color, 0.01f);
            }
        }

        matrices.pop();
        resetGlState();
    }

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

    private static Color getConfigColor() {
        ConfigItems config = Utils.getConfig();
        return new Color(
                config.red / 255f,
                config.green / 255f,
                config.blue / 255f,
                config.alpha / 255f
        );
    }

    private static void drawOutlineBox(MatrixStack matrices, VertexConsumer consumer, BlockPos pos, Vec3d camPos, Color color, float thickness) {
        double x = pos.getX() - camPos.x;
        double y = pos.getY() - camPos.y;
        double z = pos.getZ() - camPos.z;

        Vec3d[] corners = new Vec3d[]{
                new Vec3d(x, y, z),
                new Vec3d(x + 1, y, z),
                new Vec3d(x + 1, y + 1, z),
                new Vec3d(x, y + 1, z),
                new Vec3d(x, y, z + 1),
                new Vec3d(x + 1, y, z + 1),
                new Vec3d(x + 1, y + 1, z + 1),
                new Vec3d(x, y + 1, z + 1)
        };

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (int[] edge : EDGES) {
            drawLineAsQuad(consumer, matrix, corners[edge[0]], corners[edge[1]], thickness, color);
        }
    }

    private static void drawLineAsQuad(VertexConsumer consumer, Matrix4f matrix, Vec3d p1, Vec3d p2, float thickness, Color color) {
        Vec3d dir = p2.subtract(p1).normalize();
        Vec3d up = (Math.abs(dir.y) > 0.999) ? new Vec3d(1, 0, 0) : new Vec3d(0, 1, 0);
        Vec3d side = dir.crossProduct(up).normalize().multiply(thickness / 2);

        // 构建矩形线条的四个顶点
        Vec3d p1a = p1.add(side);
        Vec3d p1b = p1.subtract(side);
        Vec3d p2a = p2.add(side);
        Vec3d p2b = p2.subtract(side);

        // 绘制两个三角形组成矩形
        drawTriangle(consumer, matrix, p1a, p2a, p2b, color);
        drawTriangle(consumer, matrix, p2b, p1b, p1a, color);
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f matrix, Vec3d v1, Vec3d v2, Vec3d v3, Color color) {
        consumer.vertex(matrix, (float) v1.x, (float) v1.y, (float) v1.z)
                .color(color.r, color.g, color.b, color.a).texture(0.0f, 0.0f).normal(0, 1, 0);
        consumer.vertex(matrix, (float) v2.x, (float) v2.y, (float) v2.z)
                .color(color.r, color.g, color.b, color.a).texture(0.0f, 0.0f).normal(0, 1, 0);
        consumer.vertex(matrix, (float) v3.x, (float) v3.y, (float) v3.z)
                .color(color.r, color.g, color.b, color.a).texture(0.0f, 0.0f).normal(0, 1, 0);
    }
}

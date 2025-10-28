package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
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
import top.diaoyugan.vein_mine.utils.Utils;


public class RenderOutlines {

    public static void onInitialize() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(RenderOutlines::renderHighlights);
    }

    // 单独封装实际渲染逻辑
    private static void renderHighlights(WorldRenderContext context) {
        if(Utils.getConfig().enableHighlights) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null) return;

            setupGlState();

            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = context.camera().getPos();
            OutlineVertexConsumerProvider buffer = client.getBufferBuilders().getOutlineVertexConsumers();

            VertexConsumer consumer = buffer.getBuffer(VMRenderLayers.LINES_NO_DEPTH);
            Color color = getOutlineColor();

            for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
                drawOutlineBox(matrices, consumer, pos, camPos, color);
            }

            buffer.draw();
            restoreGlState();
        }
    }

    private static void setupGlState() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    private static void restoreGlState() {
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static Color getOutlineColor() {
        ConfigItems cfg = Utils.getConfig();
        return new Color(cfg.red / 255f, cfg.green / 255f, cfg.blue / 255f, cfg.alpha / 255f);
    }

    private static void drawOutlineBox(MatrixStack matrices, VertexConsumer consumer, BlockPos pos, Vec3d camPos, Color color) {
        Vec3d offset = new Vec3d(pos.getX() - camPos.x, pos.getY() - camPos.y, pos.getZ() - camPos.z);
        Vec3d[] corners = getCubeCorners(offset);
        int[][] edges = getCubeEdges();

        Matrix4f matrix = matrices.peek().getPositionMatrix();
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
                {0,1},{1,2},{2,3},{3,0},
                {4,5},{5,6},{6,7},{7,4},
                {0,4},{1,5},{2,6},{3,7}
        };
    }

    private static void drawLineAsQuad(VertexConsumer consumer, Matrix4f matrix, Vec3d p1, Vec3d p2, float thickness, Color color) {
        Vec3d dir = p2.subtract(p1).normalize();
        Vec3d side = dir.crossProduct(new Vec3d(0,1,0));
        if (side.lengthSquared() == 0) side = new Vec3d(1,0,0);
        side = side.normalize().multiply(thickness / 2);

        Vec3d p1a = p1.add(side), p1b = p1.subtract(side);
        Vec3d p2a = p2.add(side), p2b = p2.subtract(side);

        drawTriangle(consumer, matrix, p1a, p2a, p2b, color);
        drawTriangle(consumer, matrix, p2b, p1b, p1a, color);
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f matrix, Vec3d v1, Vec3d v2, Vec3d v3, Color color) {
        consumer.vertex(matrix, (float)v1.x,(float)v1.y,(float)v1.z)
                .color(color.r,color.g,color.b,color.a).texture(0f,0f).normal(0,1,0);
        consumer.vertex(matrix, (float)v2.x,(float)v2.y,(float)v2.z)
                .color(color.r,color.g,color.b,color.a).texture(0f,0f).normal(0,1,0);
        consumer.vertex(matrix, (float)v3.x,(float)v3.y,(float)v3.z)
                .color(color.r,color.g,color.b,color.a).texture(0f,0f).normal(0,1,0);
    }

    private record Color(float r, float g, float b, float a) {
    }
}
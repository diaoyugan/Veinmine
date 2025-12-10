package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
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

    // 12 条棱
    private static final int[][] EDGES = {
            {0,1},{1,2},{2,3},{3,0},
            {4,5},{5,6},{6,7},{7,4},
            {0,4},{1,5},{2,6},{3,7},
    };

    private record Color(float r, float g, float b, float a) {}

    // 线条风格枚举：THIN_LINES 真线、RIBBON_THICK_LINES 摄像机对齐的粗线、OLD_TRIANGLES 保留旧三角法
    private enum LineStyle {
        THIN_LINES,
        RIBBON_THICK_LINES,
        OLD_TRIANGLES
    }

    // 默认风格 —— 可改成从配置读取
    private static final LineStyle STYLE = LineStyle.RIBBON_THICK_LINES;

    // 初始化事件注册（放在 mod 初始化时调用）
    public static void init() {
        WorldRenderEvents.END_MAIN.register(context -> {
            if (!Utils.getConfig().enableHighlights) return;

            MinecraftClient client = MinecraftClient.getInstance();
            VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
            MatrixStack matrices = context.matrices();
            Camera camera = context.gameRenderer().getCamera();

            renderHighlights(matrices, immediate, camera);
        });
    }

    // 渲染高亮主逻辑
    public static void renderHighlights(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera) {
        if (MinecraftClient.getInstance().world == null || ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.isEmpty()) return;

        if (!initialized) {
            initialized = true;
            CustomLayers.init();
        }

        Vec3d camPos = camera.getCameraPos();

        prepareGlState();
        matrices.push();

        // 选择 layer：如果启用了 IntrusiveConfig，使用自定义 layer，否则用内置 lines
        RenderLayer layer = IntrusiveConfig.isEnabled() ? CustomLayers.getLinesNoDepth() : RenderLayers.lines();
        Color color = getConfigColor();

        // 获取 Matrix4f（world -> view）
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // 对每个高亮方块写顶点
        for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
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

            for (int[] edge : EDGES) {
                Vec3d p1 = corners[edge[0]];
                Vec3d p2 = corners[edge[1]];
                switch (STYLE) {
                    case THIN_LINES -> drawLineSimple(vertexConsumers.getBuffer(layer), matrix, p1, p2, color);
                    case RIBBON_THICK_LINES -> drawBillboardRibbon(vertexConsumers.getBuffer(layer), matrix, p1, p2, color, 0.04f);
                    case OLD_TRIANGLES -> drawLineAsQuad_Old(vertexConsumers.getBuffer(layer), matrix, p1, p2, 0.02f, color);
                }
            }
        }

        // 提交缓冲（必须）
        vertexConsumers.draw();

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

    // --------------------------
    // 真·线（每条线提交两个顶点 -> DrawMode.LINES）
    // --------------------------
    private static void drawLineSimple(VertexConsumer consumer, Matrix4f matrix, Vec3d p1, Vec3d p2, Color color) {
        consumer.vertex(matrix, (float)p1.x, (float)p1.y, (float)p1.z)
                .color(color.r, color.g, color.b, color.a)
                .normal(0, 1, 0);
        consumer.vertex(matrix, (float)p2.x, (float)p2.y, (float)p2.z)
                .color(color.r, color.g, color.b, color.a)
                .normal(0, 1, 0);
    }

    // --------------------------
    // 粗线：摄像机对齐的 ribbon（保留 TRIANGLES 提交）
    // --------------------------
    private static void drawBillboardRibbon(VertexConsumer consumer, Matrix4f matrix,
                                            Vec3d p1, Vec3d p2, Color color, float thickness) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d camPos = camera.getCameraPos();

        Vec3d dir = p2.subtract(p1);
        double len = dir.length();
        if (len == 0) return;
        dir = dir.normalize();

        // 摄像机方向（从点到摄像机）
        Vec3d camDir = camPos.subtract(p1).normalize();

        // 计算侧向向量 = dir x camDir（垂直于线段和平视向量）
        Vec3d side = dir.crossProduct(camDir);
        double sideLen = side.length();
        if (sideLen == 0) {
            // 在极端情况下（线段方向与摄像机方向共线），选一个任意向量保证不为零
            side = new Vec3d(0, 1, 0);
        } else {
            side = side.normalize().multiply(thickness / 2.0);
        }

        Vec3d p1a = p1.add(side);
        Vec3d p1b = p1.subtract(side);
        Vec3d p2a = p2.add(side);
        Vec3d p2b = p2.subtract(side);

        // 两个三角形组成矩形带（提交给 TRIANGLES）
        drawTriangle(consumer, matrix, p1a, p2a, p2b, color);
        drawTriangle(consumer, matrix, p2b, p1b, p1a, color);
    }

    // --------------------------
    // 旧三角实现（保留）
    // --------------------------
    private static void drawLineAsQuad_Old(VertexConsumer consumer, Matrix4f matrix, Vec3d p1, Vec3d p2, float thickness, Color color) {
        Vec3d dir = p2.subtract(p1).normalize();
        Vec3d up = (Math.abs(dir.y) > 0.999) ? new Vec3d(1, 0, 0) : new Vec3d(0, 1, 0);
        Vec3d side = dir.crossProduct(up).normalize().multiply(thickness / 2);

        Vec3d p1a = p1.add(side);
        Vec3d p1b = p1.subtract(side);
        Vec3d p2a = p2.add(side);
        Vec3d p2b = p2.subtract(side);

        drawTriangle(consumer, matrix, p1a, p2a, p2b, color);
        drawTriangle(consumer, matrix, p2b, p1b, p1a, color);
    }

    // --------------------------
    // 基础三角形提交（用于粗线与旧三角）
    // --------------------------
    private static void drawTriangle(VertexConsumer consumer, Matrix4f matrix, Vec3d v1, Vec3d v2, Vec3d v3, Color color) {
        // texture coords 设为 0,0；normal 固定为 0,1,0（用于简单渲染）
        consumer.vertex(matrix, (float) v1.x, (float) v1.y, (float) v1.z)
                .color(color.r, color.g, color.b, color.a).texture(0.0f, 0.0f).normal(0, 1, 0);
        consumer.vertex(matrix, (float) v2.x, (float) v2.y, (float) v2.z)
                .color(color.r, color.g, color.b, color.a).texture(0.0f, 0.0f).normal(0, 1, 0);
        consumer.vertex(matrix, (float) v3.x, (float) v3.y, (float) v3.z)
                .color(color.r, color.g, color.b, color.a).texture(0.0f, 0.0f).normal(0, 1, 0);
    }
}

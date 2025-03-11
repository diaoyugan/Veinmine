package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import top.diaoyugan.vein_mine.client.ClientBlockHighlighting;

import java.util.Objects;
import java.util.OptionalDouble;

public class RenderOutlines {
    public static void onInitialize(){
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (MinecraftClient.getInstance().world == null) return;

            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();
            RenderSystem.disableDepthTest();

            OutlineVertexConsumerProvider buffer = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();

            for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
                drawOutlineBox(Objects.requireNonNull(context.matrixStack()), buffer.getBuffer(RenderLayer.getLines()), pos, camPos);
            }

            buffer.draw(); // 提交渲染
            RenderSystem.enableDepthTest();
        });
//        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
//            MinecraftClient client = MinecraftClient.getInstance();
//            if (client.world == null || client.cameraEntity == null) return;
//
//            Camera camera = context.camera();
//            Vec3d camPos = camera.getPos();
//
//            MatrixStack matrices = context.matrixStack();
//
//            // 💡就在这里获取 buffer 和 consumer
//            VertexConsumerProvider.Immediate buffer = client.getBufferBuilders().getEntityVertexConsumers();
//            VertexConsumer consumer = buffer.getBuffer(OUTLINE_LINES); // 使用定义的 RenderLayer
//
//            for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
//                    drawOutlineBox(matrices, consumer, pos, camPos);
//            }
//
//            buffer.draw(); // 提交绘制
//        });

    }
    private static void drawOutlineBox(MatrixStack matrices, VertexConsumer consumer, BlockPos pos, Vec3d cameraPos) {
        double x = pos.getX() - cameraPos.x;
        double y = pos.getY() - cameraPos.y;
        double z = pos.getZ() - cameraPos.z;

        float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;//这是颜色

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // 立方体的8个点
        Vec3d[] corners = new Vec3d[]{
                new Vec3d((float)x,     (float)y,     (float)z),
                new Vec3d((float)x+1,   (float)y,     (float)z),
                new Vec3d((float)x+1,   (float)y+1,   (float)z),
                new Vec3d((float)x,     (float)y+1,   (float)z),
                new Vec3d((float)x,     (float)y,     (float)z+1),
                new Vec3d((float)x+1,   (float)y,     (float)z+1),
                new Vec3d((float)x+1,   (float)y+1,   (float)z+1),
                new Vec3d((float)x,     (float)y+1,   (float)z+1),
        };

        int[][] edges = {
                {0,1},{1,2},{2,3},{3,0},
                {4,5},{5,6},{6,7},{7,4},
                {0,4},{1,5},{2,6},{3,7},
        };

        for (int[] edge : edges) {
            Vec3d p1 = corners[edge[0]];
            Vec3d p2 = corners[edge[1]];
            consumer.vertex(matrix, (float) p1.getX(), (float) p1.getY(), (float) p1.getZ()).color(r, g, b, a).normal(0,1,0);
            consumer.vertex(matrix, (float) p2.getX(), (float) p2.getY(), (float) p2.getZ()).color(r, g, b, a).normal(0,1,0);
        }
    }
    private static final RenderLayer OUTLINE_LINES = RenderLayer.of(
            "outline_lines",
            VertexFormats.LINES,
            VertexFormat.DrawMode.LINES,
            256,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(2.0)))
                    .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                    .depthTest(RenderPhase.ALWAYS_DEPTH_TEST) // <<< 禁用深度测试，让线框穿墙
                    .cull(RenderPhase.DISABLE_CULLING)
                    .writeMaskState(RenderPhase.ALL_MASK)
                    .build(true)
    );
}
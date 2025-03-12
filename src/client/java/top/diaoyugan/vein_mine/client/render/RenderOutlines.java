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

public class RenderOutlines {
    public static void onInitialize(){
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (MinecraftClient.getInstance().world == null) return;

            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false); // 禁止写入深度缓冲
            RenderSystem.enableBlend(); // 开启混合
            RenderSystem.defaultBlendFunc(); // 默认混合模式

            OutlineVertexConsumerProvider buffer = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();

            for (BlockPos pos : ClientBlockHighlighting.HIGHLIGHTED_BLOCKS) {
                drawOutlineBox(Objects.requireNonNull(context.matrixStack()), buffer.getBuffer(RenderLayer.getLines()), pos, camPos);
            }

            buffer.draw(); // 提交渲染

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        });

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
}
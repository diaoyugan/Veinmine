package top.diaoyugan.vein_mine.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import top.diaoyugan.vein_mine.client.ClientBlockHighlighting;
import top.diaoyugan.vein_mine.config.ConfigItems;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.Set;

public final class OutlineRenderer {
    private OutlineRenderer() {}

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(OutlineRenderer::onAfterTranslucent);
    }

    private static void onAfterTranslucent(net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context) {
        Set<BlockPos> blocks = ClientBlockHighlighting.HIGHLIGHTED_BLOCKS;
        if (blocks.isEmpty()) return;

        ConfigItems cfg = Utils.getConfig();
        if (!cfg.enableHighlights) return;

        MatrixStack matrices = context.matrixStack();
        Camera camera = context.camera();
        VertexConsumerProvider consumers = context.consumers();
        if (matrices == null || camera == null || consumers == null) return;

        Vec3d camPos = camera.getPos();
        float r = cfg.red / 255f;
        float g = cfg.green / 255f;
        float b = cfg.blue / 255f;
        float a = cfg.alpha / 255f;

        VertexConsumer buffer = consumers.getBuffer(RenderLayer.getLines());

        matrices.push();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        for (BlockPos pos : blocks) {
            WorldRenderer.drawBox(matrices, buffer,
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0,
                    r, g, b, a);
        }

        matrices.pop();
    }
}

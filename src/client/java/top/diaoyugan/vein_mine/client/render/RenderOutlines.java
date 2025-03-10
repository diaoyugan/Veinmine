package top.diaoyugan.vein_mine.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gl.GlUsage;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import top.diaoyugan.vein_mine.client.ClientBlockHighlighting;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.sin;


public class RenderOutlines {
    private static VertexBuffer vertexBuffer;
    public static AtomicBoolean requestedRefresh = new AtomicBoolean(false);


    public static synchronized void render(WorldRenderContext context) {

        if (vertexBuffer == null || requestedRefresh.get()) {
            requestedRefresh.set(false);
            vertexBuffer = new VertexBuffer(GlUsage.STATIC_WRITE);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);


            for (BlockPos pos : ClientBlockHighlighting.highlightedBlocks) {
                renderBlock(buffer, pos);  // 调用 renderBlock 方法来渲染每个 BlockPos
            }

            vertexBuffer.bind();
            vertexBuffer.upload(buffer.endNullable());
            VertexBuffer.unbind();
        }

        if (vertexBuffer != null) {
            Camera camera = context.camera();
            Vec3d cameraPos = camera.getPos();

            MatrixStack poseStack = context.matrixStack();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            if (poseStack != null) {
                poseStack.push();
            }


            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystem.depthFunc(GL11.GL_ALWAYS);

            Matrix4f matrix4f = new Matrix4f(context.projectionMatrix());

            matrix4f.lookAt(cameraPos.toVector3f(), cameraPos.toVector3f().add(camera.getHorizontalPlane()), camera.getVerticalPlane());

            vertexBuffer.bind();
            vertexBuffer.draw(poseStack.peek().getPositionMatrix(), matrix4f, RenderSystem.getShader());
            VertexBuffer.unbind();

            // Reset everything
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);

            poseStack.pop();
        }
    }

    private static void renderBlock(BufferBuilder buffer, BlockPos blockPos) {
        final float size = 1.0f;
        final int x = blockPos.getX(), y = blockPos.getY(), z = blockPos.getZ();

        final float red = 255f;
        final float green = 255f;
        final float blue = 255f;

        final float opacity = 255f;
        buffer.vertex(x, y + size, z).color(red, green, blue, opacity);
        buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity);
        buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity);
        buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity);
        buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y + size, z).color(red, green, blue, opacity);

        // BOTTOM
        buffer.vertex(x + size, y, z).color(red, green, blue, opacity);
        buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity);
        buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y, z).color(red, green, blue, opacity);
        buffer.vertex(x, y, z).color(red, green, blue, opacity);
        buffer.vertex(x + size, y, z).color(red, green, blue, opacity);

        // Edge 1
        buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity);
        buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity);

        // Edge 2
        buffer.vertex(x + size, y, z).color(red, green, blue, opacity);
        buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity);

        // Edge 3
        buffer.vertex(x, y, z + size).color(red, green, blue, opacity);
        buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity);

        // Edge 4
        buffer.vertex(x, y, z).color(red, green, blue, opacity);
        buffer.vertex(x, y + size, z).color(red, green, blue, opacity);
    }
}

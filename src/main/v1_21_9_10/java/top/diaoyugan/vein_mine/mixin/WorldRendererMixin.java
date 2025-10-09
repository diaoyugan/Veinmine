package top.diaoyugan.vein_mine.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.diaoyugan.vein_mine.client.render.RenderOutlines;

import java.lang.reflect.Method;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderHead(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f matrix4f, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        try {
            VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            matrixStack.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
// renderHighlights
            matrixStack.pop();


            try {
                // 调用渲染逻辑；RenderOutlines 实现中应能接受 immediate 为 null 的情况（或做相应保护）
                RenderOutlines.renderHighlights(matrixStack, immediate, camera);
            } catch (Throwable t) {
                // 渲染保护：避免因为渲染代码崩溃影响游戏主线程
                System.err.println("[VeinMine] renderHighlights failed: " + t);
                t.printStackTrace();
            } finally {
                // 如果我们通过反射实际拿到了 immediate，尝试 flush / draw（若需要）
                if (immediate != null) {
                    try {
                        immediate.draw();
                    } catch (Throwable ignored) {
                        // best-effort flush，不要抛出
                    }
                }
            }
        } catch (Throwable t) {
            // 总体保护，防止任何反射/调用异常导致 mod/游戏崩溃
            System.err.println("[VeinMine] safe render hook failed: " + t);
            t.printStackTrace();
        }
    }

    /**
     * 运行时尝试以安全的方式从 bufferBuilders 上找到返回 VertexConsumerProvider.Immediate 的无参方法并调用。
     * 若没有找到或调用失败则返回 null（代表降级：不使用 immediate）。
     */
    @Unique
    private VertexConsumerProvider.Immediate tryGetImmediateFromBufferBuilders() {
        if (bufferBuilders == null) return null;
        try {
            Method[] methods = bufferBuilders.getClass().getMethods();
            for (Method m : methods) {
                if (m.getParameterCount() == 0) {
                    Class<?> ret = m.getReturnType();
                    if (VertexConsumerProvider.Immediate.class.isAssignableFrom(ret)) {
                        try {
                            Object res = m.invoke(bufferBuilders);
                            if (res instanceof VertexConsumerProvider.Immediate) {
                                return (VertexConsumerProvider.Immediate) res;
                            }
                        } catch (Throwable ignored) {
                            // 某些方法可能不可访问或抛异常，继续尝试其他方法
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }
}

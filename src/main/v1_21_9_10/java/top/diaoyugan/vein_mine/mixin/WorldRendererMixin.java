package top.diaoyugan.vein_mine.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import top.diaoyugan.vein_mine.client.render.RenderOutlines;
import top.diaoyugan.vein_mine.utils.Utils;
import top.diaoyugan.vein_mine.utils.logging.Logger;
import top.diaoyugan.vein_mine.utils.logging.LoggerLevels;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    /**
     * 在透明层渲染完成后（等价于 WorldRenderEvents.AFTER_TRANSLUCENT）执行。
     */
    @WrapOperation(
            method = "method_62214", // 这个是 1.21+ 的渲染主循环方法，保持不变即可 (在1.21.11后不可用)
            slice = @Slice(
                    from = @At(
                            value = "INVOKE_STRING",
                            target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",
                            args = "ldc=translucent"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/SectionRenderState;renderSection(Lnet/minecraft/client/render/BlockRenderLayerGroup;)V",
                    ordinal = 0
            )
    )
    private void afterTranslucentRender(
            SectionRenderState instance,
            BlockRenderLayerGroup group,
            Operation<Void> original
    ) {
        // 先执行原始透明层渲染
        original.call(instance, group);

        if(Utils.getConfig().enableHighlights){
            try {
                Camera camera = client.gameRenderer.getCamera();
                VertexConsumerProvider.Immediate immediate = bufferBuilders.getEntityVertexConsumers();
                MatrixStack matrices = new MatrixStack();

                // 调用渲染逻辑
                RenderOutlines.renderHighlights(matrices, immediate, camera);

            } catch (Throwable t) {
                Logger.throwLog(LoggerLevels.ERROR, String.valueOf(t));
            }
        }
    }
}

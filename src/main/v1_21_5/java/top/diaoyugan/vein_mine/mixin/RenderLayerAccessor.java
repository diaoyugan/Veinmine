package top.diaoyugan.vein_mine.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(RenderLayer.class)
public abstract class RenderLayerAccessor {
    @Invoker("of")
    public static RenderLayer.MultiPhase callOf(String name, int size, boolean hasCrumbling, boolean translucent,
                                                RenderPipeline pipeline, RenderLayer.MultiPhaseParameters params) {
        throw new AssertionError();
    }
}






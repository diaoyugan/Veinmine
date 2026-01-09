package top.diaoyugan.vein_mine.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderLayer.class)
public interface RenderLayerInvoker {

    // 这个方法会被 Mixin 自动重定向到 RenderLayer.of(...)
    @Invoker("of")
    static RenderLayer callOf(String name, RenderSetup setup) {
        throw new AssertionError();
    }
}

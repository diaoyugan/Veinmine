package top.diaoyugan.vein_mine.mixin;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderLayerInvoker {

    // 这个方法会被 Mixin 自动重定向到 RenderLayer.of(...)
    @Invoker("create")
    static RenderType callOf(String name, RenderSetup setup) {
        throw new AssertionError();
    }
}

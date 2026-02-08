package top.diaoyugan.veinmine.mixin;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeInvoker {

    // 这个方法会被 Mixin 自动重定向到 RenderLayer.of(...)
    @Invoker("create")
    static RenderType callCreate(String name, RenderSetup setup) {
        throw new AssertionError();
    }
}

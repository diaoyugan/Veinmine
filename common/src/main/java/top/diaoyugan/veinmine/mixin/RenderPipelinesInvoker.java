package top.diaoyugan.veinmine.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesInvoker {

    @Invoker("register")
    static RenderPipeline invokeRegister(RenderPipeline pipeline) {
        return null;
    }
    @Accessor("LINES_SNIPPET")
    static RenderPipeline.Snippet getLinesSnippet() {
        throw new AssertionError(); // Mixin 会生成字节码
    }
}

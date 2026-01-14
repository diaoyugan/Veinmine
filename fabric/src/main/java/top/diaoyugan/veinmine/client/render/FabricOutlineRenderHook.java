package top.diaoyugan.veinmine.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

public final class FabricOutlineRenderHook {

    public static void init() {
        LevelRenderEvents.END_MAIN.register(ctx -> {
            if (!Utils.getConfig().enableHighlights) return;

            Minecraft mc = Minecraft.getInstance();

            OutlineRenderer.render(
                    ctx.poseStack(),
                    mc.renderBuffers().bufferSource(),
                    ctx.gameRenderer().getMainCamera(),
                    ClientHighlightState.HIGHLIGHTED_BLOCKS,
                    IntrusiveConfig.isEnabled()
                            ? CustomLayers.getLinesNoDepth()
                            : RenderTypes.lines(),
                    OutlineRenderer.LineStyle.RIBBON_THICK_LINES,
                    UtilsColorHelper.fromConfig(),
                    IntrusiveConfig.isEnabled()
            );
        });
    }
}

package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

@EventBusSubscriber(modid = Constants.ID,value = Dist.CLIENT)
public final class NeoOutlineRenderHook {

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {
        if (!Utils.getConfig().enableHighlights) return;

        Minecraft mc = Minecraft.getInstance();

        OutlineRenderer.render(
                event.getPoseStack(),
                mc.renderBuffers().bufferSource(),
                mc.gameRenderer.getMainCamera(),
                ClientHighlightState.HIGHLIGHTED_BLOCKS,
                IntrusiveConfig.isEnabled()
                        ? CustomLayers.getLinesNoDepth()
                        : RenderTypes.lines(),
                OutlineRenderer.LineStyle.RIBBON_THICK_LINES,
                UtilsColorHelper.fromConfig(),
                IntrusiveConfig.isEnabled()
        );
    }
}

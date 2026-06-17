package top.diaoyugan.veinmine.client.render;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

@EventBusSubscriber(modid = Constants.ID, value = Dist.CLIENT)
public final class NeoOutlineRenderHook {
    private NeoOutlineRenderHook() {}

    @SubscribeEvent
    public static void onExtractBlockOutline(ExtractBlockOutlineRenderStateEvent event) {
        if (!Utils.getConfig().enableHighlights) return;
        if (!ClientHighlightState.SHOW_HIGHLIGHT) return;

        LineRenderProfile profile = RenderProfiles.of(IntrusiveConfig.isEnabled());
        List<BlockOutlineBuilder.Line> lines = BlockOutlineBuilder.buildLines(
                event.getCamera(),
                ClientHighlightState.HIGHLIGHTED_BLOCKS,
                UtilsColorHelper.fromConfig()
        );

        if (lines.isEmpty()) return;

        event.addCustomRenderer((state, collector, poseStack, levelRenderState) -> {
            collector.submitCustomGeometry(
                    poseStack,
                    profile.type(),
                    (pose, consumer) -> {
                        for (BlockOutlineBuilder.Line line : lines) {
                            LineRenderDispatcher.draw(
                                    consumer,
                                    pose,
                                    line,
                                    profile.style()
                            );
                        }
                    }
            );
            return true;
        });
    }
}

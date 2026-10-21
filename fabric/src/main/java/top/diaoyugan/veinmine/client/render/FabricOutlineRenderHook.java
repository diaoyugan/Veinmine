package top.diaoyugan.veinmine.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.FeatureRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.SubmitRenderPhases;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

public final class FabricOutlineRenderHook {
    private static boolean rendVanillaBlockOutline = true;

    public static void init() {
        FeatureRendererRegistry.register(SeeThroughOutlineRenderer.TYPE, SeeThroughOutlineRenderer::new);
        LevelRenderEvents.COLLECT_SUBMITS.register(ctx -> {
            rendVanillaBlockOutline = true;
            if (!Utils.getConfig().enableHighlights) return;
            if (!ClientHighlightState.SHOW_HIGHLIGHT) return;

            boolean intrusive = IntrusiveConfig.isEnabled();
            LineRenderProfile profile = RenderProfiles.of(intrusive);

            List<BlockOutlineBuilder.Line> lines = BlockOutlineBuilder.buildLines(
                    ctx.gameRenderer().mainCamera(),
                    ClientHighlightState.HIGHLIGHTED_BLOCKS,
                    UtilsColorHelper.fromConfig()
            );

            if (lines.isEmpty()) return;

            rendVanillaBlockOutline = false;

            if (intrusive) {
                ctx.submitNodeCollector().submitCustom(
                        SubmitRenderPhases.SEE_THROUGH_NAME_TAGS,
                        new SeeThroughOutlineRenderer.Submit(ctx.poseStack().last().copy(), profile, lines)
                );
                return;
            }

            ctx.submitNodeCollector().submitCustomGeometry(
                    ctx.poseStack(),
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
        });
        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((ctx,state)
                -> rendVanillaBlockOutline);
    }
}

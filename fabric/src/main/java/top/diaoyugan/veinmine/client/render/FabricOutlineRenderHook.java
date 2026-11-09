package top.diaoyugan.veinmine.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.FeatureRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.SubmitRenderPhases;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

public final class FabricOutlineRenderHook {
    private static boolean renderVanillaBlockOutline = true;

    private FabricOutlineRenderHook() {}

    public static void init() {
        FeatureRendererRegistry.register(SeeThroughOutlineRenderer.TYPE, SeeThroughOutlineRenderer::new);
        LevelRenderEvents.COLLECT_SUBMITS.register(FabricOutlineRenderHook::submitHighlights);
        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, state) -> renderVanillaBlockOutline);
    }

    private static void submitHighlights(LevelRenderContext context) {
        renderVanillaBlockOutline = true;
        if (!Utils.getConfig().enableHighlights || !ClientHighlightState.SHOW_HIGHLIGHT) return;

        List<BlockOutlineBuilder.Line> lines = BlockOutlineBuilder.buildLines(
                context.gameRenderer().mainCamera(),
                ClientHighlightState.HIGHLIGHTED_BLOCKS,
                UtilsColorHelper.fromConfig()
        );
        if (lines.isEmpty()) return;

        boolean seeThrough = IntrusiveConfig.isEnabled();
        LineRenderProfile profile = RenderProfiles.of(seeThrough);
        renderVanillaBlockOutline = false;

        if (seeThrough) {
            context.submitNodeCollector().submitCustom(
                    SubmitRenderPhases.SEE_THROUGH_NAME_TAGS,
                    new SeeThroughOutlineRenderer.Submit(context.poseStack().last().copy(), profile, lines)
            );
            return;
        }

        context.submitNodeCollector().submitCustomGeometry(
                context.poseStack(),
                profile.type(),
                (pose, consumer) -> LineRenderDispatcher.drawAll(consumer, pose, lines, profile.style())
        );
    }
}

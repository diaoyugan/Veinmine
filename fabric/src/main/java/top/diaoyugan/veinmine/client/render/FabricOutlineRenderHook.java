package top.diaoyugan.veinmine.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

public final class FabricOutlineRenderHook {
    private static boolean rendVanillaBlockOutline = true;

    public static void init() {
        LevelRenderEvents.COLLECT_SUBMITS.register(ctx -> {
            rendVanillaBlockOutline = true;
            if (!Utils.getConfig().enableHighlights) return;
            if (!ClientHighlightState.SHOW_HIGHLIGHT) return;

            LineRenderProfile profile = RenderProfiles.of(Utils.getConfig());

            List<OutlineBuilder.Line> lines = OutlineBuilder.buildLines(
                    ctx.gameRenderer().mainCamera(),
                    ClientHighlightState.HIGHLIGHTED_BLOCKS,
                    UtilsColorHelper.fromConfig()
            );

            if (lines.isEmpty()) return;

            rendVanillaBlockOutline = false;

            ctx.submitNodeCollector().submitCustomGeometry(
                    ctx.poseStack(),
                    profile.type(),
                    (pose, consumer) -> {
                        for (OutlineBuilder.Line line : lines) {
                            LineRenderer.draw(
                                    consumer,
                                    pose,
                                    ctx.gameRenderer().mainCamera(),
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
package top.diaoyugan.veinmine.fabric.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.client.render.BlockOutlineBuilder;
import top.diaoyugan.veinmine.client.render.LineRenderDispatcher;
import top.diaoyugan.veinmine.client.render.LineRenderProfile;
import top.diaoyugan.veinmine.client.render.RenderProfiles;
import top.diaoyugan.veinmine.client.render.UtilsColorHelper;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

public final class FabricOutlineRenderHook {
    private static boolean rendVanillaBlockOutline = true;

    public static void init() {
        LevelRenderEvents.COLLECT_SUBMITS.register(ctx -> {
            rendVanillaBlockOutline = true;
            if (!Utils.getConfig().enableHighlights) return;
            if (!ClientHighlightState.SHOW_HIGHLIGHT) return;

            LineRenderProfile profile = RenderProfiles.of(IntrusiveConfig.isEnabled());

            List<BlockOutlineBuilder.Line> lines = BlockOutlineBuilder.buildLines(
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

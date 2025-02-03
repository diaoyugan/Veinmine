package top.diaoyugan.vein_mine.client.config;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import top.diaoyugan.vein_mine.Config;

public class VeinmineConfigScreen extends Screen { // Hold the current config
    private Config config;
    private final Screen parent;
    protected VeinmineConfigScreen(Screen parent) {
        super(Text.translatable("vm.config.config_screen_title"));
        this.parent = parent;
    }
    @Override
    protected void init() {
        this.config = Config.load();
        ConfigBuilder cb = ConfigBuilder.create().setParentScreen(this.parent).setTitle(Text.translatable("vm.config.screen.title"));
        cb.setSavingRunnable(config::save);
        Screen screen = initConfigScreen(cb, config);
        this.client.setScreen(screen);
    }

    @Override
    public void render(DrawContext dc,int mouseX, int mouseY, float delta) {
        this.renderBackground(dc, mouseX, mouseY, delta);
        super.render(dc, mouseX, mouseY, delta);
    }

    private Optional<Integer> parseIntSafe(String s){
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    private Screen initConfigScreen(ConfigBuilder cb, Config config){
        ConfigCategory mainConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.main"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
        /* Search Radius */
        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.search_radius"), Config.getCurrentConfig().searchRadius, 1, 10)
                .setTooltip(Text.translatable("vm.config.search_radius.tooltip"))
                .setDefaultValue(1)
                .setSaveConsumer(i -> config.modify("searchRadius", i))
                .build());
        /* BFS Limit */
        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.bfs_limit"), Config.getCurrentConfig().BFSLimit, 1, 128)
                .setDefaultValue(50)
                .setSaveConsumer(i -> config.modify("BFSLimit", i))
                .build());

        /* Ignored Blocks */
        List<String> ignoredBlocks = new ArrayList<>(Config.getCurrentConfig().ignoredBlocks);
        mainConfig.addEntry(entryBuilder.startStrList(Text.translatable("vm.config.ignored_blocks"), ignoredBlocks)
                .setSaveConsumer(strings -> {
                    Set<String> newIgnoredBlocks = Set.copyOf(strings);
                    config.modify("ignoredBlocks", newIgnoredBlocks);
                })
                .build());
        /* Use BFS */
        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_bfs"), Config.getCurrentConfig().useBFS)
                .setDefaultValue(true)
                .setSaveConsumer(b -> config.modify("useBFS", b))
                .build());
        /* Use Radius Search */
        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_radius_search"), Config.getCurrentConfig().useRadiusSearch)
                .setTooltip(Text.translatable("vm.config.use_radius_search.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> config.modify("useRadiusSearch", b))
                .build());
        /* Use Radius Search When Reach BFS Limit */
        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_radius_search_when_reach_bfs_limit"), Config.getCurrentConfig().useRadiusSearchWhenReachBFSLimit)
                .setTooltip(Text.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> config.modify("useRadiusSearchWhenReachBFSLimit", b))
                .build());

        return cb.build();
    }
}

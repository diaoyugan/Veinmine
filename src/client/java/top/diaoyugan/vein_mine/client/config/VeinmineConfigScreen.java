package top.diaoyugan.vein_mine.client.config;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

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
        config = Config.load();
        ConfigBuilder cb = ConfigBuilder.create().setParentScreen(this.parent).setTitle(Text.translatable("vm.config.screen.title"));
        cb.setSavingRunnable(config::save);
        Screen screen = initConfigScreen(cb);
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
    private Screen initConfigScreen(ConfigBuilder cb){

        ConfigCategory mainConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.mining"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.search_radius"), Config.getCurrentConfig().searchRadius, 1, 10)
                .setTooltip(Text.translatable("vm.config.search_radius.tooltip"))
                .setDefaultValue(1)
                .setSaveConsumer(i -> config.modify("searchRadius", i, ""))
                .build());
        return cb.build();
    }
}

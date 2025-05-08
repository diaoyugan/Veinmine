package top.diaoyugan.vein_mine.client.config;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.Config;
import top.diaoyugan.vein_mine.ConfigItems;
import top.diaoyugan.vein_mine.client.vein_mineClient;

public class VeinmineConfigScreen extends Screen { // Hold the current config
    private Config config;
    private ConfigItems configItems;
    private final Screen parent;
    protected VeinmineConfigScreen(Screen parent) {
        super(Text.translatable("vm.config.screen.title"));
        this.parent = parent;
    }
    @Override
    protected void init() {
        this.config = Config.getInstance();
        configItems = config.getConfigItems();
        ConfigBuilder cb = ConfigBuilder.create().setParentScreen(this.parent).setTitle(Text.translatable("vm.config.screen.title"));
        cb.setSavingRunnable(config::save);
        Screen screen = initConfigScreen(cb, configItems);
        this.client.setScreen(screen);
    }

    @Override
    public void render(DrawContext dc,int mouseX, int mouseY, float delta) {
        this.renderBackground(dc, mouseX, mouseY, delta);
        super.render(dc, mouseX, mouseY, delta);
    }

    private Screen initConfigScreen(ConfigBuilder cb, ConfigItems ci){
        configItems.keyBindingCode = KeyBindingHelper.getBoundKeyOf(vein_mineClient.BINDING).getCode();
        ConfigCategory mainConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.main"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        mainConfig.addEntry(entryBuilder
                .startTextDescription(Text.translatable("vm.config.transnote"))
                .build());

        /* Search Radius */
        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.search_radius"), ci.searchRadius, 1, 10)
                .setTooltip(Text.translatable("vm.config.search_radius.tooltip"))
                .setDefaultValue(1)
                .setSaveConsumer(i -> configItems.searchRadius = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.block", value))
                .build());
        /* BFS Limit */
        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.bfs_limit"), ci.BFSLimit, 1, 128)
                .setTooltip(Text.translatable("vm.config.bfs_limit.tooltip"))
                .setDefaultValue(50)
                .setSaveConsumer(i -> configItems.BFSLimit = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.block", value))
                .build());

        /* Ignored Blocks */
        List<String> ignoredBlocks = new ArrayList<>(ci.ignoredBlocks);
        mainConfig.addEntry(entryBuilder.startStrList(Text.translatable("vm.config.ignored_blocks"), ignoredBlocks)
                .setTooltip(Text.translatable("vm.config.ignored_blocks.tooltip"))
                .setSaveConsumer(strings -> configItems.ignoredBlocks = Set.copyOf(strings))
                .build());
        /* Use BFS */
        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_bfs"), ci.useBFS)
                .setTooltip(Text.translatable("vm.config.use_bfs.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> configItems.useBFS = b)
                .build());
        /* Use Radius Search */
        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_radius_search"), ci.useRadiusSearch)
                .setTooltip(Text.translatable("vm.config.use_radius_search.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> configItems.useRadiusSearch = b)
                .build());
        /* Use Radius Search When Reach BFS Limit */
        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_radius_search_when_reach_bfs_limit"), ci.useRadiusSearchWhenReachBFSLimit)
                .setTooltip(Text.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> configItems.useRadiusSearchWhenReachBFSLimit = b)
                .build());

        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.highlight_blocks_message"), ci.highlightBlocksMessage)
                .setTooltip(Text.translatable("vm.config.highlight_blocks_message.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> configItems.highlightBlocksMessage = b)
                .build());

        ConfigCategory toolsAndProtectConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.toolsandprotect"));
        ConfigEntryBuilder toolsAndProtectEntryBuilder = cb.entryBuilder();

        /* Protect Tools */
        toolsAndProtectConfig.addEntry(toolsAndProtectEntryBuilder.startBooleanToggle(Text.translatable("vm.config.protect_tools"), ci.protectTools)
                .setTooltip(Text.translatable("vm.config.protect_tools.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> configItems.protectTools = b)
                .build());
        /* Protected Tools */
        List<String> protectedTools = new ArrayList<>(ci.protectedTools);
        toolsAndProtectConfig.addEntry(toolsAndProtectEntryBuilder.startStrList(Text.translatable("vm.config.protected_tools"), protectedTools)
                .setTooltip(Text.translatable("vm.config.protected_tools.tooltip"))
                .setSaveConsumer(strings -> configItems.protectedTools = Set.copyOf(strings))
                .build());
        /* Protect All Default Valuable Tools */
        toolsAndProtectConfig.addEntry(toolsAndProtectEntryBuilder.startBooleanToggle(Text.translatable("vm.config.protect_allValuable_tools"), ci.protectAllDefaultValuableTools)
                .setTooltip(Text.translatable("vm.config.protect_allValuable_tools.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(b -> configItems.protectAllDefaultValuableTools = b)
                .build());
        /* Durability Threshold */
        toolsAndProtectConfig.addEntry(toolsAndProtectEntryBuilder.startIntSlider(Text.translatable("vm.config.durability_threshold"), ci.durabilityThreshold, 1, 256)
                .setTooltip(Text.translatable("vm.config.durability_threshold.tooltip"))
                .setDefaultValue(10)
                .setSaveConsumer(i -> configItems.durabilityThreshold = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.durability", value))
                .build());

        ConfigCategory highlightsConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.highlights"));
        ConfigEntryBuilder hlEntryBuilder = cb.entryBuilder();

        var redSlider = hlEntryBuilder.startIntSlider(Text.translatable("vm.config.renderRed"), ci.red, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(val -> configItems.red = val)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .build();

        var greenSlider = hlEntryBuilder.startIntSlider(Text.translatable("vm.config.renderGreen"), ci.green, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(val -> configItems.green = val)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .build();

        var blueSlider = hlEntryBuilder.startIntSlider(Text.translatable("vm.config.renderBlue"), ci.blue, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(val -> configItems.blue = val)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .build();


        highlightsConfig.addEntry(new ColorPreviewEntry(
                redSlider::getValue,
                greenSlider::getValue,
                blueSlider::getValue
        ));

        highlightsConfig.addEntry(hlEntryBuilder.startIntSlider(Text.translatable("vm.config.renderAlpha"), ci.alpha, 0, 255)
                .setDefaultValue(255)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .setSaveConsumer(val -> configItems.alpha = val)
                .setTextGetter(value -> Text.literal(String.format("%.0f%%", value / 255.0 * 100)))
                .build());

        highlightsConfig.addEntry(redSlider);
        highlightsConfig.addEntry(greenSlider);
        highlightsConfig.addEntry(blueSlider);

        highlightsConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.renderTime"), ci.renderTime, 1, 3)
                .setTooltip(Text.translatable("vm.config.renderTime.tooltip"))
                .setDefaultValue(1)
                .setSaveConsumer(i -> configItems.renderTime = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.times", value))
                .build());

        ConfigCategory keysAndBinding = cb.getOrCreateCategory(Text.translatable("vm.config.screen.keysAndBinding"));
        ConfigEntryBuilder keysAndBindingEntryBuilder = cb.entryBuilder();

        keysAndBinding.addEntry(keysAndBindingEntryBuilder.startBooleanToggle(Text.translatable("vm.config.use_hold_instead_of_toggle"), ci.useHoldInsteadOfToggle)
                .setTooltip(Text.translatable("vm.config.use_hold_instead_of_toggle.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(b -> configItems.useHoldInsteadOfToggle = b)
                .build());

        keysAndBinding.addEntry(keysAndBindingEntryBuilder.startKeyCodeField(
                        Text.translatable("key.vm.switch"),
                        InputUtil.fromKeyCode(ci.keyBindingCode, 0))
                .setTooltip(Text.translatable("key.vm.switch.tooltip"))
                .setDefaultValue(InputUtil.fromKeyCode(GLFW.GLFW_KEY_GRAVE_ACCENT, 0))
                .setKeySaveConsumer(key -> {
                    configItems.keyBindingCode = key.getCode(); // 存储为 int
                    vein_mineClient.updateKeyBinding(key.getCode()); // 重新注册按键
                })
                .build());




        return cb.build();
    }
}

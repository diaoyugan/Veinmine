package top.diaoyugan.vein_mine.client.configScreen;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.networking.keybindreciever.KeybindingPayload;
import top.diaoyugan.vein_mine.config.Config;
import top.diaoyugan.vein_mine.config.ConfigItems;
import top.diaoyugan.vein_mine.client.vein_mineClient;
import top.diaoyugan.vein_mine.utils.Utils;

public class VeinmineConfigScreen extends Screen {
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
        this.configItems = config.getConfigItems();
        ConfigBuilder cb = ConfigBuilder.create()
                .setParentScreen(this.parent)
                .setTitle(Text.translatable("vm.config.screen.title"));
        cb.setSavingRunnable(config::save);

        Screen screen = initConfigScreen(cb, configItems);
        if (this.client != null) {
            this.client.setScreen(screen);
        } else {
            throw new IllegalStateException("Client not initialized");
        }
    }

    @Override
    public void render(DrawContext dc, int mouseX, int mouseY, float delta) {
        this.renderBackground(dc, mouseX, mouseY, delta);
        super.render(dc, mouseX, mouseY, delta);
    }

    private Screen initConfigScreen(ConfigBuilder cb, ConfigItems ci) {
        configItems.keyBindingCode = KeyBindingHelper.getBoundKeyOf(vein_mineClient.BINDING).getCode();

        createMainConfig(cb, ci);
        createToolsAndProtectConfig(cb, ci);
        createHighlightsConfig(cb, ci);
        createKeysAndBindingConfig(cb, ci);
        createAdvanceConfig(cb, ci);

        return cb.build();
    }

    private void createMainConfig(ConfigBuilder cb, ConfigItems ci) {
        ConfigCategory mainConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.main"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        mainConfig.addEntry(entryBuilder.startTextDescription(Text.translatable("vm.config.translation_note")).build());

        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.search_radius"), ci.searchRadius, 1, 10)
                .setTooltip(Text.translatable("vm.config.search_radius.tooltip"))
                .setDefaultValue(1)
                .setSaveConsumer((Integer i) -> configItems.searchRadius = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.block", value))
                .build());

        mainConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.bfs_limit"), ci.BFSLimit, 1, 128)
                .setTooltip(Text.translatable("vm.config.bfs_limit.tooltip"))
                .setDefaultValue(50)
                .setSaveConsumer((Integer i) -> configItems.BFSLimit = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.block", value))
                .build());

        List<String> ignoredBlocks = new ArrayList<>(ci.ignoredBlocks);
        mainConfig.addEntry(entryBuilder.startStrList(Text.translatable("vm.config.ignored_blocks"), ignoredBlocks)
                .setTooltip(Text.translatable("vm.config.ignored_blocks.tooltip"))
                .setSaveConsumer((List<String> strings) -> configItems.ignoredBlocks = Set.copyOf(strings))
                .build());

        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_bfs"), ci.useBFS)
                .setTooltip(Text.translatable("vm.config.use_bfs.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.useBFS = b)
                .build());

        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_radius_search"), ci.useRadiusSearch)
                .setTooltip(Text.translatable("vm.config.use_radius_search.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.useRadiusSearch = b)
                .build());

        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_radius_search_when_reach_bfs_limit"), ci.useRadiusSearchWhenReachBFSLimit)
                .setTooltip(Text.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.useRadiusSearchWhenReachBFSLimit = b)
                .build());

        mainConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.highlight_blocks_message"), ci.highlightBlocksMessage)
                .setTooltip(Text.translatable("vm.config.highlight_blocks_message.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.highlightBlocksMessage = b)
                .build());
    }

    private void createToolsAndProtectConfig(ConfigBuilder cb, ConfigItems ci) {
        ConfigCategory toolsConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.toolsandprotect"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        toolsConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.protect_tools"), ci.protectTools)
                .setTooltip(Text.translatable("vm.config.protect_tools.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.protectTools = b)
                .build());

        List<String> protectedTools = new ArrayList<>(ci.protectedTools);
        toolsConfig.addEntry(entryBuilder.startStrList(Text.translatable("vm.config.protected_tools"), protectedTools)
                .setTooltip(Text.translatable("vm.config.protected_tools.tooltip"))
                .setSaveConsumer((List<String> strings) -> configItems.protectedTools = Set.copyOf(strings))
                .build());

        toolsConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.protect_allValuable_tools"), ci.protectAllDefaultValuableTools)
                .setTooltip(Text.translatable("vm.config.protect_allValuable_tools.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.protectAllDefaultValuableTools = b)
                .build());

        toolsConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.durability_threshold"), ci.durabilityThreshold, 1, 256)
                .setTooltip(Text.translatable("vm.config.durability_threshold.tooltip"))
                .setDefaultValue(10)
                .setSaveConsumer((Integer i) -> configItems.durabilityThreshold = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.durability", value))
                .build());
    }

    private void createHighlightsConfig(ConfigBuilder cb, ConfigItems ci) {
        ConfigCategory highlightsConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.highlights"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        var redSlider = entryBuilder.startIntSlider(Text.translatable("vm.config.renderRed"), ci.red, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer((Integer val) -> configItems.red = val)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .build();

        var greenSlider = entryBuilder.startIntSlider(Text.translatable("vm.config.renderGreen"), ci.green, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer((Integer val) -> configItems.green = val)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .build();

        var blueSlider = entryBuilder.startIntSlider(Text.translatable("vm.config.renderBlue"), ci.blue, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer((Integer val) -> configItems.blue = val)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .build();

        highlightsConfig.addEntry(new ColorPreviewEntry(redSlider::getValue, greenSlider::getValue, blueSlider::getValue));
        highlightsConfig.addEntry(redSlider);
        highlightsConfig.addEntry(greenSlider);
        highlightsConfig.addEntry(blueSlider);

        highlightsConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.renderAlpha"), ci.alpha, 0, 255)
                .setDefaultValue(255)
                .setTextGetter(val -> Text.literal(String.valueOf(val)))
                .setSaveConsumer((Integer val) -> configItems.alpha = val)
                .setTextGetter(value -> Text.literal(String.format("%.0f%%", value / 255.0 * 100)))
                .build());

        highlightsConfig.addEntry(entryBuilder.startIntSlider(Text.translatable("vm.config.renderTime"), ci.renderTime, 1, 3)
                .setTooltip(Text.translatable("vm.config.renderTime.tooltip"))
                .setDefaultValue(1)
                .setSaveConsumer((Integer i) -> configItems.renderTime = i)
                .setTextGetter(value -> Text.translatable("vm.config.value.times", value))
                .build());
    }

    private void createKeysAndBindingConfig(ConfigBuilder cb, ConfigItems ci) {
        ConfigCategory keysConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.keysAndBinding"));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        keysConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.use_hold_instead_of_toggle"), ci.useHoldInsteadOfToggle)
                .setTooltip(Text.translatable("vm.config.use_hold_instead_of_toggle.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer((Boolean b) -> {
                    if (client != null && client.player != null) {
                        if (Utils.getVeinMineSwitchState(client.player)) {
                            ClientPlayNetworking.send(KeybindingPayload.INSTANCE);
                        }
                    }
                    configItems.useHoldInsteadOfToggle = b;
                })
                .build());

        keysConfig.addEntry(entryBuilder.startKeyCodeField(Text.translatable("key.vm.switch"), InputUtil.fromKeyCode(ci.keyBindingCode, 0))
                .setTooltip(Text.translatable("key.vm.switch.tooltip"))
                .setDefaultValue(InputUtil.fromKeyCode(GLFW.GLFW_KEY_GRAVE_ACCENT, 0))
                .setKeySaveConsumer((InputUtil.Key key) -> {
                    configItems.keyBindingCode = key.getCode();
                    vein_mineClient.updateKeyBinding(key.getCode());
                })
                .build());
    }

    private void createAdvanceConfig(ConfigBuilder cb, ConfigItems ci) {
        ConfigCategory finalConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.final_resort").styled(style -> style.withColor(Formatting.RED)));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        finalConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.useIntrusiveCode").styled(style -> style.withColor(Formatting.RED)), ci.useIntrusiveCode)
                .setTooltip(Text.translatable("vm.config.useIntrusiveCode.tooltip").styled(style -> style.withColor(Formatting.RED)))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.useIntrusiveCode = b)
                .build());
    }
}
//package top.diaoyugan.vein_mine.client.configScreen;
//
//import me.shedaniel.clothconfig2.api.ConfigCategory;
//import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
//import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.minecraft.ChatFormatting;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.network.chat.Component;
//import me.shedaniel.clothconfig2.api.ConfigBuilder;
//import com.mojang.blaze3d.platform.InputConstants;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import top.diaoyugan.vein_mine.client.ClientVersionInterface;
//import top.diaoyugan.vein_mine.client.CLInterfaceOverride;
//import top.diaoyugan.vein_mine.client.HotKeys;
//import top.diaoyugan.vein_mine.networking.keypacket.KeyPressPacket;
//import top.diaoyugan.vein_mine.config.Config;
//import top.diaoyugan.vein_mine.config.ConfigItems;
//
//
//public class VeinmineConfigScreen extends Screen {
//    protected Config config;
//    private ConfigItems configItems;
//    private final Screen parent;
//    ClientVersionInterface CLI = new CLInterfaceOverride();
//
//    protected VeinmineConfigScreen(Screen parent) {
//        super(Component.translatable("vm.config.screen.title"));
//        this.parent = parent;
//    }
//
//    @Override
//    protected void init() {
//        this.config = Config.getInstance();
//        this.configItems = config.getConfigItems();
//        ConfigBuilder cb = ConfigBuilder.create()
//                .setParentScreen(this.parent)
//                .setTitle(Component.translatable("vm.config.screen.title"));
//        cb.setSavingRunnable(config::save);
//
//        Screen screen = initConfigScreen(cb, configItems);
//        if (this.minecraft != null) {
//            this.minecraft.setScreen(screen);
//        } else {
//            throw new IllegalStateException("Client not initialized");
//        }
//    }
//
//    @Override
//    public void render(GuiGraphics dc, int mouseX, int mouseY, float delta) {
//        this.renderBackground(dc, mouseX, mouseY, delta);
//        super.render(dc, mouseX, mouseY, delta);
//    }
//
//    private Screen initConfigScreen(ConfigBuilder cb, ConfigItems ci) {
//        configItems.keyBindingCode = KeyBindingHelper.getBoundKeyOf(CLI.getBinding()).getValue();
//        IVersionConfigProvider vp = CommonConfig.getVersionProvider();
//
//        createMainConfig(cb, ci);
//        createToolsAndProtectConfig(cb, ci);
//        createHighlightsConfig(cb, ci);
//        createKeysAndBindingConfig(cb, ci);
//
//        if (vp.allowOption("use_intrusive_code")) {
//            createAdvanceConfig(cb, ci);
//        }
//
//        return cb.build();
//    }
//
//    private void createMainConfig(ConfigBuilder cb, ConfigItems ci) {
//        ConfigCategory mainConfig = cb.getOrCreateCategory(Component.translatable("vm.config.screen.main"));
//        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
//
//        mainConfig.addEntry(entryBuilder.startTextDescription(Component.translatable("vm.config.translation_note")).build());
//
//        mainConfig.addEntry(entryBuilder.startIntSlider(Component.translatable("vm.config.search_radius"), ci.searchRadius, 1, 10)
//                .setTooltip(Component.translatable("vm.config.search_radius.tooltip"))
//                .setDefaultValue(1)
//                .setSaveConsumer((Integer i) -> configItems.searchRadius = i)
//                .setTextGetter(value -> Component.translatable("vm.config.value.block", value))
//                .build());
//
//        mainConfig.addEntry(entryBuilder.startIntSlider(Component.translatable("vm.config.bfs_limit"), ci.BFSLimit, 1, 128)
//                .setTooltip(Component.translatable("vm.config.bfs_limit.tooltip"))
//                .setDefaultValue(50)
//                .setSaveConsumer((Integer i) -> configItems.BFSLimit = i)
//                .setTextGetter(value -> Component.translatable("vm.config.value.block", value))
//                .build());
//
//        List<String> ignoredBlocks = new ArrayList<>(ci.ignoredBlocks);
//        mainConfig.addEntry(entryBuilder.startStrList(Component.translatable("vm.config.ignored_blocks"), ignoredBlocks)
//                .setTooltip(Component.translatable("vm.config.ignored_blocks.tooltip"))
//                .setSaveConsumer((List<String> strings) -> configItems.ignoredBlocks = Set.copyOf(strings))
//                .build());
//
//        mainConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.use_bfs"), ci.useBFS)
//                .setTooltip(Component.translatable("vm.config.use_bfs.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.useBFS = b)
//                .build());
//
//        mainConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.use_radius_search"), ci.useRadiusSearch)
//                .setTooltip(Component.translatable("vm.config.use_radius_search.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.useRadiusSearch = b)
//                .build());
//
//        mainConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit"), ci.useRadiusSearchWhenReachBFSLimit)
//                .setTooltip(Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.useRadiusSearchWhenReachBFSLimit = b)
//                .build());
//
//        mainConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.highlight_blocks_message"), ci.highlightBlocksMessage)
//                .setTooltip(Component.translatable("vm.config.highlight_blocks_message.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.highlightBlocksMessage = b)
//                .build());
//    }
//
//    private void createToolsAndProtectConfig(ConfigBuilder cb, ConfigItems ci) {
//        ConfigCategory toolsConfig = cb.getOrCreateCategory(Component.translatable("vm.config.screen.toolsandprotect"));
//        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
//
//        toolsConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.protect_tools"), ci.protectTools)
//                .setTooltip(Component.translatable("vm.config.protect_tools.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.protectTools = b)
//                .build());
//
//        List<String> protectedTools = new ArrayList<>(ci.protectedTools);
//        toolsConfig.addEntry(entryBuilder.startStrList(Component.translatable("vm.config.protected_tools"), protectedTools)
//                .setTooltip(Component.translatable("vm.config.protected_tools.tooltip"))
//                .setSaveConsumer((List<String> strings) -> configItems.protectedTools = Set.copyOf(strings))
//                .build());
//
//        toolsConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.protect_allValuable_tools"), ci.protectAllDefaultValuableTools)
//                .setTooltip(Component.translatable("vm.config.protect_allValuable_tools.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.protectAllDefaultValuableTools = b)
//                .build());
//
//        toolsConfig.addEntry(entryBuilder.startIntSlider(Component.translatable("vm.config.durability_threshold"), ci.durabilityThreshold, 1, 256)
//                .setTooltip(Component.translatable("vm.config.durability_threshold.tooltip"))
//                .setDefaultValue(10)
//                .setSaveConsumer((Integer i) -> configItems.durabilityThreshold = i)
//                .setTextGetter(value -> Component.translatable("vm.config.value.durability", value))
//                .build());
//    }
//
//    private void createHighlightsConfig(ConfigBuilder cb, ConfigItems ci) {
//        ConfigCategory highlightsConfig = cb.getOrCreateCategory(Component.translatable("vm.config.screen.highlights"));
//        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
//
//        highlightsConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.enableHighlights"), ci.enableHighlights)
//                .setTooltip(Component.translatable("vm.config.enableHighlights.tooltip"))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.enableHighlights = b)
//                .build());
//
//        var redSlider = entryBuilder.startIntSlider(Component.translatable("vm.config.renderRed"), ci.red, 0, 255)
//                .setDefaultValue(255)
//                .setSaveConsumer((Integer val) -> configItems.red = val)
//                .setTextGetter(val -> Component.literal(String.valueOf(val)))
//                .build();
//
//        var greenSlider = entryBuilder.startIntSlider(Component.translatable("vm.config.renderGreen"), ci.green, 0, 255)
//                .setDefaultValue(255)
//                .setSaveConsumer((Integer val) -> configItems.green = val)
//                .setTextGetter(val -> Component.literal(String.valueOf(val)))
//                .build();
//
//        var blueSlider = entryBuilder.startIntSlider(Component.translatable("vm.config.renderBlue"), ci.blue, 0, 255)
//                .setDefaultValue(255)
//                .setSaveConsumer((Integer val) -> configItems.blue = val)
//                .setTextGetter(val -> Component.literal(String.valueOf(val)))
//                .build();
//
//        highlightsConfig.addEntry(new ColorPreviewEntry(redSlider::getValue, greenSlider::getValue, blueSlider::getValue));
//        highlightsConfig.addEntry(redSlider);
//        highlightsConfig.addEntry(greenSlider);
//        highlightsConfig.addEntry(blueSlider);
//
//        highlightsConfig.addEntry(entryBuilder.startIntSlider(Component.translatable("vm.config.renderAlpha"), ci.alpha, 0, 255)
//                .setDefaultValue(255)
//                .setTextGetter(val -> Component.literal(String.valueOf(val)))
//                .setSaveConsumer((Integer val) -> configItems.alpha = val)
//                .setTextGetter(value -> Component.literal(String.format("%.0f%%", value / 255.0 * 100)))
//                .build());
//
//        highlightsConfig.addEntry(entryBuilder.startIntSlider(Component.translatable("vm.config.renderTime"), ci.renderTime, 1, 3)
//                .setTooltip(Component.translatable("vm.config.renderTime.tooltip"))
//                .setDefaultValue(1)
//                .setSaveConsumer((Integer i) -> configItems.renderTime = i)
//                .setTextGetter(value -> Component.translatable("vm.config.value.times", value))
//                .build());
//    }
//
//    private void createKeysAndBindingConfig(ConfigBuilder cb, ConfigItems ci) {
//        ConfigCategory keysConfig = cb.getOrCreateCategory(Component.translatable("vm.config.screen.keysAndBinding"));
//        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
//
//        keysConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.use_hold_instead_of_toggle"), ci.useHoldInsteadOfToggle)
//                .setTooltip(Component.translatable("vm.config.use_hold_instead_of_toggle.tooltip"))
//                .setDefaultValue(false)
//                .setSaveConsumer((Boolean b) -> {
//                    if (minecraft != null && minecraft.player != null) {
//                        if (HotKeys.getVeinMineSwitchState()) {
//                            ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
//                        }
//                    }
//                    configItems.useHoldInsteadOfToggle = b;
//                })
//                .build());
//
//        keysConfig.addEntry(
//                entryBuilder.startKeyCodeField(
//                                Component.translatable("key.vm.switch"),
//                                CLI.getConfigKey(ci.keyBindingCode)  // 从接口拿配置键位
//                        )
//                        .setTooltip(Component.translatable("key.vm.switch.tooltip"))
//                        .setDefaultValue(CLI.getDefaultKey())  // 从接口拿默认键位
//                        .setKeySaveConsumer((InputConstants.Key key) -> {
//                            configItems.keyBindingCode = key.getValue();
//                            CLI.UpDateKeyBinding(key.getValue());
//                        })
//                        .build()
//        );
//
//    }
//
//    public static void createAdvanceConfig(ConfigBuilder cb, ConfigItems ci) {
//        Config config = Config.getInstance();
//        ConfigItems configItems = config.getConfigItems();
//        ConfigCategory finalConfig = cb.getOrCreateCategory(Component.translatable("vm.config.screen.final_resort").withStyle(style -> style.withColor(ChatFormatting.RED)));
//        ConfigEntryBuilder entryBuilder = cb.entryBuilder();
//
//        finalConfig.addEntry(entryBuilder.startBooleanToggle(Component.translatable("vm.config.useIntrusiveCode").withStyle(style -> style.withColor(ChatFormatting.RED)), ci.useIntrusiveCode)
//                .setTooltip(Component.translatable("vm.config.useIntrusiveCode.tooltip").withStyle(style -> style.withColor(ChatFormatting.RED)))
//                .setDefaultValue(true)
//                .setSaveConsumer((Boolean b) -> configItems.useIntrusiveCode = b)
//                .build());
//    }
//}
package top.diaoyugan.veinmine.client.configScreen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import top.diaoyugan.enchanted_ui.api.client.gui.EnchantedUI;
import top.diaoyugan.enchanted_ui.api.client.gui.UIBottomBar;
import top.diaoyugan.enchanted_ui.api.client.gui.UIForm;
import top.diaoyugan.enchanted_ui.api.client.gui.UIFormSpec;
import top.diaoyugan.enchanted_ui.api.client.gui.UIScreenStyle;
import top.diaoyugan.enchanted_ui.api.client.gui.UITabbedScreen;
import top.diaoyugan.veinmine.client.inputs.KeyBinding;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.config.ConfigItems;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigScreen extends UITabbedScreen {

    private final Screen parent;
    private final ConfigItems items = Utils.getConfig();
    private final DraftConfig draft;
    private final int entryMinChar = 3;

    public ConfigScreen(Screen parent) {
        super(parent, Component.translatable("vm.config.screen.title"));
        this.parent = parent;
        this.draft = DraftConfig.from(items);

        tab(10, 30, 20,
                Component.translatable("vm.config.screen.main"),
                EnchantedUI.formPage(200, mainPage()));
        tab(10, 54, 20,
                Component.translatable("vm.config.screen.toolsandprotect"),
                EnchantedUI.formPage(200, toolsAndProtectPage()));
        tab(10, 78, 20,
                Component.translatable("vm.config.screen.highlights"),
                EnchantedUI.formPage(200, highlightsPage()));
        tab(10, 102, 20,
                Component.translatable("vm.config.screen.keysAndBinding"),
                EnchantedUI.formPage(200, keysAndBindingsPage()));
        tab(10, 126, 20,
                Component.translatable("vm.config.screen.final_resort"),
                Style.EMPTY.withColor(ChatFormatting.RED),
                EnchantedUI.formPage(200, advancedPage()));

        style(UIScreenStyle.builder()
                .bottomBarBlur(true)
                .bottomBarBackgroundColor(0x55101010)
                .bottomBarSeparatorColor(0x44FFFFFF)
                .build());

        bottomBar(UIBottomBar.saveAndCloseWithExtra(
                Component.translatable("vm.config.close"),
                Component.translatable("vm.config.save_and_exit"),
                this::saveDraft,
                Component.literal("🔄"),
                Tooltip.create(Component.translatable("vm.config.screen.reset.tooltip")),
                this::resetConfig
        ));
    }

    private UIFormSpec mainPage() {
        return form -> {
            form.title(Component.translatable("vm.config.screen.main"));
            addMainToggles(form);
            addMainSliders(form);
            form.space(4);
//            form.title(Component.translatable("vm.config.ignored_blocks"));
//            form.textArea(
//                    Component.translatable("vm.config.ignored_blocks"),
//                    30,
//                    () -> draft.ignoredBlocksText,
//                    value -> draft.ignoredBlocksText = value
//            ).setTooltip(Tooltip.create(Component.translatable("vm.config.ignored_blocks.tooltip")));
            form.title(Component.translatable("vm.config.ignored_blocks"));
            form.editableDropdownList(
                    Component.translatable("vm.config.ignored_blocks"),
                    200,
                    () -> draft.ignoredBlocks,
                    entries -> {
                        draft.ignoredBlocks.clear();
                        draft.ignoredBlocks.addAll(entries);
                    },
                    Component.translatable("vm.config.screen.add_entry"),
                    Component.translatable("vm.config.screen.add"),
                    3,
                    value -> value.length() < entryMinChar ?
                            Component.translatable("vm.config.screen.least_characters", entryMinChar)
                            : null,
                    false
            ).setTooltip(Tooltip.create(Component.translatable("vm.config.ignored_blocks.tooltip")));
        };
    }

    private void addMainToggles(UIForm form) {
        var row = form.toggleRow(
                Component.translatable("vm.config.use_bfs"),
                () -> draft.useBFS,
                value -> draft.useBFS = value,

                Component.translatable("vm.config.use_radius_search"),
                () -> draft.useRadiusSearch,
                value -> draft.useRadiusSearch = value
        );
        row.getFirst().tooltip(
                Component.translatable("vm.config.use_bfs.tooltip")
        );

        row.getLast().tooltip(
                Component.translatable("vm.config.use_radius_search.tooltip")
        );

        form.toggle(
                Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit"),
                () -> draft.useRadiusSearchWhenReachBFSLimit,
                value -> draft.useRadiusSearchWhenReachBFSLimit = value
        ).tooltip(Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"));
        form.toggle(
                Component.translatable("vm.config.highlight_blocks_message"),
                () -> draft.highlightBlocksMessage,
                value -> draft.highlightBlocksMessage = value
        ).tooltip(Component.translatable("vm.config.highlight_blocks_message.tooltip"));
    }

    private void addMainSliders(UIForm form) {
        form.intSlider(
                Component.translatable("vm.config.bfs_limit"),
                1,
                256,
                () -> draft.BFSLimit,
                value -> draft.BFSLimit = value,
                false
        ).setCustomValueKey("vm.config.value.block");

        form.intSlider(
                Component.translatable("vm.config.search_radius"),
                1,
                10,
                () -> draft.searchRadius,
                value -> draft.searchRadius = value,
                false
        ).setCustomValueKey("vm.config.value.block");
    }

    private UIFormSpec toolsAndProtectPage() {
        return form -> {
            form.title(Component.translatable("vm.config.screen.toolsandprotect"));
            form.toggle(
                    Component.translatable("vm.config.protect_tools"),
                    () -> draft.protectTools,
                    value -> draft.protectTools = value
            ).tooltip(Component.translatable("vm.config.protect_tools.tooltip"));

            form.toggle(
                    Component.translatable("vm.config.protect_allValuable_tools"),
                    () -> draft.protectAllDefaultValuableTools,
                    value -> draft.protectAllDefaultValuableTools = value
            ).tooltip(Component.translatable("vm.config.protect_allValuable_tools.tooltip"));

            form.intSlider(
                    Component.translatable("vm.config.durability_threshold"),
                    1,
                    256,
                    () -> draft.durabilityThreshold,
                    value -> draft.durabilityThreshold = value,
                    false
            ).setCustomValueKey("vm.config.value.durability");

            form.space(4);
            form.title(Component.translatable("vm.config.protected_tools"));
            form.editableDropdownList(
                    Component.translatable("vm.config.protected_tools"),
                    200,
                    () -> draft.protectedTools,
                    entries -> {
                        draft.protectedTools.clear();
                        draft.protectedTools.addAll(entries);
                    },
                    Component.translatable("vm.config.screen.add_entry"),
                    Component.translatable("vm.config.screen.add"),
                    3,
                    value -> value.length() < entryMinChar ?
                            Component.translatable("vm.config.screen.least_characters", entryMinChar)
                            : null,
                    false
            ).setTooltip(Tooltip.create(Component.translatable("vm.config.protected_tools.tooltip")));
        };
    }

    private UIFormSpec highlightsPage() {
        return form -> {
            form.title(Component.translatable("vm.config.screen.highlights"));
            form.toggle(
                    Component.translatable("vm.config.enableHighlights"),
                    () -> draft.enableHighlights,
                    value -> draft.enableHighlights = value
            ).tooltip(Component.translatable("vm.config.enableHighlights.tooltip"));

            form.rgbaSlidersWithPreview(
                    Component.translatable("vm.config.color_preview"),
                    () -> draft.red,
                    value -> draft.red = value,
                    () -> draft.green,
                    value -> draft.green = value,
                    () -> draft.blue,
                    value -> draft.blue = value,
                    () -> draft.alpha,
                    value -> draft.alpha = value,
                    true
            );
        };
    }

    private UIFormSpec keysAndBindingsPage() {
        return form -> {
            form.title(Component.translatable("vm.config.screen.keysAndBinding"));
            form.keyBinding(
                    Component.translatable("key.vm.switch"),
                    () -> draft.activationKey,
                    value -> draft.activationKey = value,
                    () -> draft.activationKey.getDisplayName(),
                    KeyBinding.ACTIVATION_KEY,
                    false
            ).tooltip(Component.translatable("vm.config.keybinds.tooltip"));

            form.combinationKeyBinding(
                    Component.translatable("vm.config.config_screen_keys"),
                    () -> draft.configScreenKey,
                    value -> {
                        draft.configScreenKey.clear();
                        draft.configScreenKey.addAll(value);
                    }
            ).tooltip(Component.translatable("vm.config.keybinds.combination.tooltip"));

            form.toggle(
                    Component.translatable("vm.config.use_hold_instead_of_toggle"),
                    () -> draft.useHoldInsteadOfToggle,
                    value -> draft.useHoldInsteadOfToggle = value
            ).tooltip(Component.translatable("vm.config.use_hold_instead_of_toggle.tooltip"));
        };
    }

    private UIFormSpec advancedPage() {
        return form -> {
            form.title(
                    Component.translatable("vm.config.screen.final_resort")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
            );
            form.toggle(
                    Component.translatable("vm.config.useIntrusiveCode").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)),
                    () -> draft.useIntrusiveCode,
                    value -> draft.useIntrusiveCode = value
            ).tooltip(Component.translatable("vm.config.useIntrusiveCode.tooltip"));
        };
    }

    private boolean saveDraft() {
        items.useIntrusiveCode = draft.useIntrusiveCode;
        items.searchRadius = draft.searchRadius;
        items.BFSLimit = draft.BFSLimit;
        items.useBFS = draft.useBFS;
        items.useRadiusSearch = draft.useRadiusSearch;
        items.useRadiusSearchWhenReachBFSLimit = draft.useRadiusSearchWhenReachBFSLimit;
        items.highlightBlocksMessage = draft.highlightBlocksMessage;
        items.protectTools = draft.protectTools;
        items.durabilityThreshold = draft.durabilityThreshold;
        items.protectAllDefaultValuableTools = draft.protectAllDefaultValuableTools;
        items.enableHighlights = draft.enableHighlights;
        items.red = draft.red;
        items.green = draft.green;
        items.blue = draft.blue;
        items.alpha = draft.alpha;
        items.useHoldInsteadOfToggle = draft.useHoldInsteadOfToggle;

        items.ignoredBlocks.clear();
        items.ignoredBlocks.addAll(draft.ignoredBlocks);

        items.protectedTools.clear();
        items.protectedTools.addAll(draft.protectedTools);

        items.configScreenKey.clear();
        items.configScreenKey.addAll(draft.configScreenKey);

        KeyBinding.ACTIVATION_KEY.setKey(draft.activationKey);
        KeyMapping.resetMapping();
        Minecraft.getInstance().options.save();

        Config.getInstance().save();
        return true;
    }

    private void resetConfig() {
        Config.getInstance().reset();
        minecraft.setScreenAndShow(new ConfigScreen(parent));
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }

    private static final class DraftConfig {
        private boolean useIntrusiveCode;
        private int searchRadius;
        private int BFSLimit;
        private boolean useBFS;
        private boolean useRadiusSearch;
        private boolean useRadiusSearchWhenReachBFSLimit;
        private boolean highlightBlocksMessage;
        private boolean protectTools;
        private int durabilityThreshold;
        private boolean protectAllDefaultValuableTools;
        private boolean enableHighlights;
        private int red;
        private int green;
        private int blue;
        private int alpha;
        private boolean useHoldInsteadOfToggle;
        private InputConstants.Key activationKey;
        private final Set<Integer> configScreenKey = new HashSet<>();
        private List<String> ignoredBlocks;
        private List<String> protectedTools;

        private static DraftConfig from(ConfigItems items) {
            DraftConfig draft = new DraftConfig();
            draft.useIntrusiveCode = items.useIntrusiveCode;
            draft.searchRadius = items.searchRadius;
            draft.BFSLimit = items.BFSLimit;
            draft.useBFS = items.useBFS;
            draft.useRadiusSearch = items.useRadiusSearch;
            draft.useRadiusSearchWhenReachBFSLimit = items.useRadiusSearchWhenReachBFSLimit;
            draft.highlightBlocksMessage = items.highlightBlocksMessage;
            draft.protectTools = items.protectTools;
            draft.durabilityThreshold = items.durabilityThreshold;
            draft.protectAllDefaultValuableTools = items.protectAllDefaultValuableTools;
            draft.enableHighlights = items.enableHighlights;
            draft.red = items.red;
            draft.green = items.green;
            draft.blue = items.blue;
            draft.alpha = items.alpha;
            draft.useHoldInsteadOfToggle = items.useHoldInsteadOfToggle;
            draft.activationKey = InputConstants.getKey(KeyBinding.ACTIVATION_KEY.saveString());
            draft.configScreenKey.addAll(items.configScreenKey);
            draft.ignoredBlocks = new ArrayList<>(items.ignoredBlocks);
            draft.protectedTools = new ArrayList<>(items.protectedTools);
            return draft;
        }
    }
    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);

        String text = "'Enchanted UI' Framework Dev (built-in)";

        g.centeredText(
                this.font,
                Component.literal(text),
                width - 100,
                height - 9,
                0xFF777777
        );
    }
}

package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import top.diaoyugan.veinmine.client.configScreen.pages.*;
import top.diaoyugan.veinmine.config.Config;

import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.ConfigItems;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final ConfigItems items = Utils.getConfig();

    private final List<List<AbstractWidget>> pages = new ArrayList<>();
    public int currentPage = 0;
    private final List<PendingTab> pendingTabs = new ArrayList<>();
    private static final int TAB_MIN_WIDTH = 60;
    private static final int TAB_PADDING = 10;

    private ConfigMainPage mainPage;
    private ConfigAdvancedPage advancedPage;
    private ConfigHighlightsPage highlightsPage;
    private ConfigToosAndProtectPage toosAndProtectPage;
    private ConfigKeysAndBindingsPage keysAndBindingsPage;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("vm.config.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2 + 10;

        pages.clear();

        // Page 1 index 0
        mainPage = new ConfigMainPage(items);
        pages.add(mainPage.build(centerX));

        // Page 2 index 1
        toosAndProtectPage = new ConfigToosAndProtectPage(items);
        pages.add(toosAndProtectPage.build(centerX));

        // Page 3 index 2
        highlightsPage = new ConfigHighlightsPage(items);
        pages.add(highlightsPage.build(centerX));

        // Page 4 index 3
        keysAndBindingsPage = new ConfigKeysAndBindingsPage(items);
        pages.add(keysAndBindingsPage.build(centerX));

        // Page 5 index 4
        advancedPage = new ConfigAdvancedPage(items);
        pages.add(advancedPage.build(centerX));

        addTabButton(10, 30,  Component.translatable("vm.config.screen.main"),
                20, () -> showPage(0));
        addTabButton(10, 54,  Component.translatable("vm.config.screen.toolsandprotect"),
                20, () -> showPage(1));
        addTabButton(10, 78,  Component.translatable("vm.config.screen.highlights"),
                20, () -> showPage(2));
        addTabButton(10, 102,  Component.translatable("vm.config.screen.keysAndBinding"),
                20, () -> showPage(3));
        addTabButton(10, 126, Component.translatable("vm.config.screen.final_resort")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), 20, () -> showPage(4));

        showPage(currentPage);
        buildTabButtons();
        buildButtons();
    }

    private void buildButtons() {
        buildTabButtons();

        int centerX = width / 2;
        int bottomY = height - 28;

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.close"),
                        b -> onClose())
                .bounds(centerX - 154, bottomY, 150, 20)
                .build());

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.save_and_exit"),
                        b -> saveAndExit())
                .bounds(centerX + 4, bottomY, 150, 20)
                .build());
    }

    private void addTabButton(
            int x, int y,
            Component text,
            int height,
            Runnable action
    ) {
        pendingTabs.add(new PendingTab(x, y, height, text, action));
    }

    private void buildTabButtons() {
        Font font = minecraft.font;

        int width = TAB_MIN_WIDTH;
        for (PendingTab tab : pendingTabs) {
            width = Math.max(width, font.width(tab.text) + TAB_PADDING);
        }
        for (PendingTab tab : pendingTabs) {
            Button btn = Button.builder(tab.text, b -> tab.action.run())
                    .bounds(tab.x, tab.y, width, tab.height)
                    .build();
            addRenderableWidget(btn);
        }

        pendingTabs.clear();
    }



    public void showPage(int index) {
        if (currentPage < pages.size()) {
            for (AbstractWidget w : pages.get(currentPage)) {
                removeWidget(w);
            }
        }

        for (AbstractWidget w : pages.get(index)) {
            addRenderableWidget(w);
        }

        currentPage = index;
    }

    private void saveAndExit() {
        mainPage.save();
        toosAndProtectPage.save();
        Config.getInstance().save();
        minecraft.setScreen(parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        g.fill(
                0, height - 45,
                width, height,
                0x55000000
        );
        g.fill(
                0, height - 45,
                width, height - 45 + 1,
                0xFF888888
        );
        g.fillGradient(
                0, height - 45 - 1,
                width, height - 45,
                0x00000000,
                0x55000000
        );
        super.extractRenderState(g, mouseX, mouseY, delta);
        g.text(
                this.font,
                Component.translatable("vm.config.screen.title"),
                11,
                14,
                0xFFFFFFFF
        );
        g.centeredText(
                this.font,
                Component.translatable("vm.config.saveTip"),
                width / 2,
                height - 40,
                0xFFFFFFFF
        );
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}

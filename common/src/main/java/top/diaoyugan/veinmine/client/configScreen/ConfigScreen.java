package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import top.diaoyugan.veinmine.client.configScreen.pages.*;
import top.diaoyugan.veinmine.config.Config;

import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final ConfigItems items = Config.getInstance().getConfigItems();

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
        int centerX = width / 2;

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

        addTabButton(10, 10,  Component.translatable("vm.config.screen.main"),
                20, () -> showPage(0));
        addTabButton(10, 34,  Component.translatable("vm.config.screen.toolsandprotect"),
                20, () -> showPage(1));
        addTabButton(10, 58,  Component.translatable("vm.config.screen.highlights"),
                20, () -> showPage(2));
        addTabButton(10, 82,  Component.translatable("vm.config.screen.keysAndBinding"),
                20, () -> showPage(3));
        addTabButton(10, 106, Component.translatable("vm.config.screen.final_resort")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), 20, () -> showPage(4));

        buildTabButtons();


        int bottomY = height - 28;

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.cancel"),
                        b -> onClose())
                .bounds(centerX - 154, bottomY, 150, 20)
                .build());

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.save_and_exit"),
                        b -> saveAndExit())
                .bounds(centerX + 4, bottomY, 150, 20)
                .build());

        showPage(0);
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
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}

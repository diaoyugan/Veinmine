package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import top.diaoyugan.veinmine.client.configScreen.widget.BooleanOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.IntSliderOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.config.Config;

import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final ConfigItems items = Config.getInstance().getConfigItems();


    private final List<List<AbstractWidget>> pages = new ArrayList<>();
    private int currentPage = 0;


    public ConfigScreen(Screen parent) {
        super(Component.translatable("vm.config.screen.title"));
        this.parent = parent;
    }

    public <T extends GuiEventListener & Renderable & NarratableEntry> T registerRenderableWidget(T widget) {
        this.addRenderableWidget(widget);
        return widget;
    }

    private MultiLineEditBox ignoredBlocksBox;

    @Override
    protected void init() {
        int centerX = width / 2;

        // ----------------------
        //  创建页
        // ----------------------
        List<AbstractWidget> page1Widgets = new ArrayList<>();
        List<AbstractWidget> page2Widgets = new ArrayList<>();
        List<AbstractWidget> page3Widgets = new ArrayList<>();
        pages.add(page1Widgets);
        pages.add(page2Widgets);
        pages.add(page3Widgets);

        int originalY = 30;
        // ----------------------
        //  页面1控件
        // ----------------------
        int y = originalY;
        int x = centerX - 100;
        page1Widgets.add(new TitleWidget(x, y, Component.translatable("vm.config.screen.main")));
        y += 16;

        // Boolean options and sliders...
        BooleanOptionWidget useBFSOption = new BooleanOptionWidget(centerX - 100, y, 200, 20,
                Component.translatable("vm.config.use_bfs"),
                () -> items.useBFS,
                v -> items.useBFS = v);
        useBFSOption.tooltip(Component.translatable("vm.config.use_bfs.tooltip"));
        page1Widgets.add(useBFSOption);

        y += 24;
        BooleanOptionWidget useRadiusSearchOption = new BooleanOptionWidget(centerX - 100, y, 200, 20,
                Component.translatable("vm.config.use_radius_search"),
                () -> items.useRadiusSearch,
                v -> items.useRadiusSearch = v);
        useRadiusSearchOption.tooltip(Component.translatable("vm.config.use_radius_search.tooltip"));
        page1Widgets.add(useRadiusSearchOption);

        y += 24;
        IntSliderOptionWidget bfsLimitOption = new IntSliderOptionWidget(centerX - 100, y, 200, 20,
                Component.translatable("vm.config.bfs_limit"),
                1, 256,
                () -> items.BFSLimit,
                v -> items.BFSLimit = v);
        bfsLimitOption.tooltip(Component.translatable("vm.config.bfs_limit.tooltip"));
        page1Widgets.add(bfsLimitOption);

        y += 24;
        IntSliderOptionWidget searchRadiusOption = new IntSliderOptionWidget(centerX - 100, y, 200, 20,
                Component.translatable("vm.config.search_radius"),
                1, 10,
                () -> items.searchRadius,
                v -> items.searchRadius = v);
        searchRadiusOption.tooltip(Component.translatable("vm.config.search_radius.tooltip"));
        page1Widgets.add(searchRadiusOption);
        y += 24;

        BooleanOptionWidget useRadiusSearchWhenReachBfsLimitOption = new BooleanOptionWidget(
                centerX - 100, y, 200, 20,
                Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit"),
                () -> items.useRadiusSearchWhenReachBFSLimit,
                v -> items.useRadiusSearchWhenReachBFSLimit = v
        );
        useRadiusSearchWhenReachBfsLimitOption.tooltip(Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"));
        page1Widgets.add(useRadiusSearchWhenReachBfsLimitOption);
        y += 24;

        BooleanOptionWidget highlightBlocksMessageOption = new BooleanOptionWidget(
                centerX - 100, y, 200, 20,
                Component.translatable("vm.config.highlight_blocks_message"),
                () -> items.highlightBlocksMessage,
                v -> items.highlightBlocksMessage = v
        );
        highlightBlocksMessageOption.tooltip(Component.translatable("vm.config.highlight_blocks_message.tooltip"));
        page1Widgets.add(highlightBlocksMessageOption);
        y += 24;

// 标题
        TitleWidget ignoredBlocksTitle = new TitleWidget(
                centerX - 100,
                y,
                Component.translatable("vm.config.ignored_blocks")
        );
        page1Widgets.add(ignoredBlocksTitle);
        y += 16;

// 多行输入框（真正可交互的部分）
        ignoredBlocksBox = MultiLineEditBox.builder()
                        .setX(centerX - 100)
                        .setY(y)
                        .setShowBackground(true)
                        .setShowDecorations(true)
                        .build(
                                minecraft.font,
                                200,
                                40,
                                Component.translatable("vm.config.ignored_blocks")
                        );

        ignoredBlocksBox.setValue(String.join("\n", items.ignoredBlocks));
        ignoredBlocksBox.setTooltip(
                Tooltip.create(Component.translatable("vm.config.ignored_blocks.tooltip"))
        );

        page1Widgets.add(ignoredBlocksBox);



        // ----------------------
        //  页面2和页面3占位
        // ----------------------
        page2Widgets.add(new TitleWidget(x, originalY, Component.translatable("vm.config.screen.toolsandprotect")));
        page3Widgets.add(new TitleWidget(x, originalY, Component.translatable("vm.config.screen.highlights")));

        // ----------------------
        //  顶部翻页按钮
        // ----------------------
        int btnX = 10;
        int btnY = 10;
        int btnW = 60;
        int btnH = 20;

        addRenderableWidget(Button.builder(Component.translatable("vm.config.screen.main"), b -> showPage(0))
                .bounds(btnX, btnY, btnW, btnH)
                .build());

        addRenderableWidget(Button.builder(Component.translatable("vm.config.screen.toolsandprotect"), b -> showPage(1))
                .bounds(btnX, btnY + btnSpacingH(btnH,1), btnW, btnH)
                .build());

        addRenderableWidget(Button.builder(Component.translatable("vm.config.screen.highlights"), b -> showPage(2))
                .bounds(btnX, btnY + btnSpacingH(btnH,2), btnW, btnH)
                .build());

        // ----------------------
        //  底部操作按钮
        // ----------------------
        int bottomY = height - 28;
        addRenderableWidget(Button.builder(Component.translatable("vm.config.cancel"), b -> discardAndExit())
                .bounds(centerX - 154, bottomY, 150, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("vm.config.save_and_exit"), b -> saveAndExit())
                .bounds(centerX + 4, bottomY, 150, 20)
                .build());

        // 显示初始页
        showPage(0);
    }

    private int btnSpacingH(int btnH, int index) {
        return (btnH + 4) * index;
    }

    private void saveAndExit() {
        // 保存 Set 编辑器的内容
        saveToSet(ignoredBlocksBox, items.ignoredBlocks);
        Config.getInstance().save();
        minecraft.setScreen(parent);
    }

    public void saveToSet(MultiLineEditBox editBox, Set<String> set) {
        String text = editBox.getValue();
        set.clear();
        for (String line : text.split("\n")) {
            if (!line.isBlank()) {
                set.add(line.trim());
            }
        }
    }

    private void discardAndExit() {
        minecraft.setScreen(parent);
    }

    private void showPage(int pageIndex) {
        // 移除当前已注册的页面控件
        if (currentPage < pages.size()) {
            for (AbstractWidget w : pages.get(currentPage)) {
                removeWidget(w);
            }
        }
        // 添加新页控件
        for (AbstractWidget w : pages.get(pageIndex)) {
            addRenderableWidget(w);
        }
        currentPage = pageIndex;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        // 只渲染当前页
        for (AbstractWidget w : pages.get(currentPage)) {
            w.render(g, mouseX, mouseY, delta);
        }
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

}

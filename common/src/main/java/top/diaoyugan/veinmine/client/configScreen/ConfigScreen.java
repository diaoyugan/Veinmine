package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import top.diaoyugan.veinmine.client.configScreen.widget.BooleanOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.IntSliderOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.config.Config;

import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.ConfigItems;

public class ConfigScreen extends Screen {
    ConfigItems items = Config.getInstance().getConfigItems();

    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("vm.config.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int y = 32;

        //标题
        addRenderableWidget(
                new TitleWidget(
                        centerX - 100,
                        y,
                        Component.translatable("vm.config.screen.main")
                )
        );
        y += 16;

        //配置项
        BooleanOptionWidget use_bfsOption = new BooleanOptionWidget(
                width / 2 - 100, y, 200, 20,
                Component.translatable("vm.config.use_bfs"),
                () -> items.useBFS,
                v -> items.useBFS = v
        );
        use_bfsOption.tooltip(Component.translatable("vm.config.use_bfs.tooltip"));
        addRenderableWidget(use_bfsOption);

        BooleanOptionWidget use_radius_searchOption = new BooleanOptionWidget(
                        centerX - 100,
                        y + 24,
                        200,
                        20,
                        Component.translatable("vm.config.use_radius_search"),
                        () -> items.useRadiusSearch,
                        v -> items.useRadiusSearch = v
                );
        use_radius_searchOption.tooltip(Component.translatable("vm.config.use_radius_search.tooltip"));
        addRenderableWidget(use_radius_searchOption);

        IntSliderOptionWidget bfs_limitOption = new IntSliderOptionWidget(//这是int条 只能是整数
                        width / 2 - 100, // X
                        y + 48,           // Y
                        200,             // 宽度
                        20,              // 高度
                        Component.translatable("vm.config.bfs_limit"), // 标签
                        1, 256,           // min/max
                        () -> items.BFSLimit,           // getter: 读取缓存值
                        v -> items.BFSLimit = v // setter
                );
        bfs_limitOption.tooltip(Component.translatable("vm.config.bfs_limit.tooltip"));
        addRenderableWidget(bfs_limitOption);

        IntSliderOptionWidget search_radiusOption = new IntSliderOptionWidget(
                        width / 2 - 100,
                        y + 72,
                        200,
                        20,
                        Component.translatable("vm.config.search_radius"),
                        1, 10,
                        () -> items.searchRadius,
                        v -> items.searchRadius = v
                );
        search_radiusOption.tooltip(Component.translatable("vm.config.search_radius.tooltip"));
        addRenderableWidget(search_radiusOption);


        BooleanOptionWidget useRadiusSearchWhenReachBFSLimitOption = new BooleanOptionWidget(
                centerX - 100,
                y + 96,
                200,
                20,
                Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit"),
                () -> items.useRadiusSearchWhenReachBFSLimit,
                v -> items.useRadiusSearchWhenReachBFSLimit = v
        );
        useRadiusSearchWhenReachBFSLimitOption.tooltip(Component.translatable("vm.config.use_radius_search_when_reach_bfs_limit.tooltip"));
        addRenderableWidget(useRadiusSearchWhenReachBFSLimitOption);

        BooleanOptionWidget highlightBlocksMessageOption = new BooleanOptionWidget(
                centerX - 100,
                y + 120,
                200,
                20,
                Component.translatable("vm.config.highlight_blocks_message"),
                () -> items.highlightBlocksMessage,
                v -> items.highlightBlocksMessage = v
        );
        highlightBlocksMessageOption.tooltip(Component.translatable("vm.config.highlight_blocks_message.tooltip"));
        addRenderableWidget(highlightBlocksMessageOption);


        int btnY = height - 28;

        addRenderableWidget(
                Button.builder(
                        Component.translatable("vm.config.cancel"),
                        b -> discardAndExit()
                ).bounds(width / 2 - 154, btnY, 150, 20).build()
        );
        addRenderableWidget(
                Button.builder(
                        Component.translatable("vm.config.save_and_exit"),
                        b -> saveAndExit()
                ).bounds(width / 2 + 4, btnY, 150, 20).build()
        );
    }

    private void saveAndExit() {
        Config.getInstance().save();

        minecraft.setScreen(parent);
    }

    private void discardAndExit() {
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



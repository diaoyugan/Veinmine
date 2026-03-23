package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.configScreen.widget.VerticalLayout;
import top.diaoyugan.veinmine.client.configScreen.widget.BooleanOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.IntSliderOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class ConfigMainPage {

    private final ConfigItems items;
    private MultiLineEditBox ignoredBlocksBox;
    private IntSliderOptionWidget BFSLimitSlider;
    private IntSliderOptionWidget searchRadiusSlider;

    public ConfigMainPage(ConfigItems items) {
        this.items = items;
    }

    public List<AbstractWidget> build(int centerX) {
        List<AbstractWidget> widgets = new ArrayList<>();

        final int contentWidth = 200;
        final int leftX = centerX - contentWidth / 2;

        VerticalLayout layout = new VerticalLayout(leftX, 10, 4);
        int halfWidth = (contentWidth - 4) / 2;

        // 标题
        widgets.add(new TitleWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.screen.main")
        ));
        layout.next(10);

        widgets.add(bool(
                layout.x(),
                layout.y(),
                halfWidth,
                "vm.config.use_bfs",
                () -> items.useBFS,
                v -> items.useBFS = v
        ));

        widgets.add(bool(
                layout.x() + halfWidth + 4,
                layout.y(),
                halfWidth,
                "vm.config.use_radius_search",
                () -> items.useRadiusSearch,
                v -> items.useRadiusSearch = v
        ));
        layout.next(20);

        BFSLimitSlider = intSlider(
                layout,
                contentWidth,
                "vm.config.bfs_limit",
                1, 256,
                () -> items.BFSLimit,
                v -> items.BFSLimit = v,
                false
        );
        BFSLimitSlider.setCustomValueKey("vm.config.value.block");
        widgets.add(BFSLimitSlider);

        layout.next(20);

        searchRadiusSlider = intSlider(
                layout,
                contentWidth,
                "vm.config.search_radius",
                1, 10,
                () -> items.searchRadius,
                v -> items.searchRadius = v,
                false
        );
        searchRadiusSlider.setCustomValueKey("vm.config.value.block");
        widgets.add(searchRadiusSlider);

        layout.next(20);

        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.use_radius_search_when_reach_bfs_limit",
                () -> items.useRadiusSearchWhenReachBFSLimit,
                v -> items.useRadiusSearchWhenReachBFSLimit = v
        ));
        layout.next(20);

        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.highlight_blocks_message",
                () -> items.highlightBlocksMessage,
                v -> items.highlightBlocksMessage = v
        ));
        layout.next(24);

        widgets.add(new TitleWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.ignored_blocks")
        ));
        layout.next(10);

        ignoredBlocksBox = MultiLineEditBox.builder()
                .setX(layout.x())
                .setY(layout.y())
                .setShowBackground(true)
                .setShowDecorations(true)
                .build(
                        Minecraft.getInstance().font,
                        contentWidth,
                        30,
                        Component.translatable("vm.config.ignored_blocks")
                );

        ignoredBlocksBox.setValue(String.join("\n", items.ignoredBlocks));
        ignoredBlocksBox.setTooltip(
                Tooltip.create(Component.translatable("vm.config.ignored_blocks.tooltip"))
        );

        widgets.add(ignoredBlocksBox);

        return widgets;
    }

    public void save() {
        items.ignoredBlocks.clear();
        for (String line : ignoredBlocksBox.getValue().split("\n")) {
            if (!line.isBlank()) {
                items.ignoredBlocks.add(line.trim());
            }
        }
    }

    private BooleanOptionWidget bool(
            VerticalLayout layout,
            int width,
            String key,
            BooleanSupplier getter,
            Consumer<Boolean> setter
    ) {
        BooleanOptionWidget widget = new BooleanOptionWidget(
                layout.x(),
                layout.y(),
                width,
                20,
                Component.translatable(key),
                getter,
                setter
        );
        widget.tooltip(Component.translatable(key + ".tooltip"));
        return widget;
    }

    private BooleanOptionWidget bool(
            int x,
            int y,
            int width,
            String key,
            BooleanSupplier getter,
            Consumer<Boolean> setter
    ) {
        BooleanOptionWidget widget = new BooleanOptionWidget(
                x,
                y,
                width,
                20,
                Component.translatable(key),
                getter,
                setter
        );
        widget.setHalfWidth();
        widget.tooltip(Component.translatable(key + ".tooltip"));
        return widget;
    }

    private IntSliderOptionWidget intSlider(
            VerticalLayout layout,
            int width,
            String key,
            int min,
            int max,
            IntSupplier getter,
            IntConsumer setter,
            Boolean isPercentage
    ) {
        IntSliderOptionWidget widget = new IntSliderOptionWidget(
                layout.x(),
                layout.y(),
                width,
                20,
                Component.translatable(key),
                min,
                max,
                getter,
                setter,
                isPercentage
        );
        widget.tooltip(Component.translatable(key + ".tooltip"));
        return widget;
    }
}

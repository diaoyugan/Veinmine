package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.configScreen.widget.BooleanOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.IntSliderOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.VerticalLayout;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class ConfigToosAndProtectPage {

    private final ConfigItems items;
    private MultiLineEditBox protectedToolsBox;
    private IntSliderOptionWidget durabilityThresholdSlider;

    public ConfigToosAndProtectPage(ConfigItems items) {
        this.items = items;
    }

    public List<AbstractWidget> build(int centerX) {
        List<AbstractWidget> widgets = new ArrayList<>();

        final int contentWidth = 200;
        final int leftX = centerX - contentWidth / 2;

        VerticalLayout layout = new VerticalLayout(leftX, 30, 4);

        // 标题
        widgets.add(new TitleWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.screen.toolsandprotect")
        ));
        layout.next(16);

        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.protect_tools",
                () -> items.protectTools,
                v -> items.protectTools = v
        ));
        layout.next(20);

        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.protect_allValuable_tools",
                () -> items.protectAllDefaultValuableTools,
                v -> items.protectAllDefaultValuableTools = v
        ));
        layout.next(20);

        durabilityThresholdSlider = intSlider(
                layout,
                contentWidth,
                "vm.config.durability_threshold",
                1, 256,
                () -> items.durabilityThreshold,
                v -> items.durabilityThreshold = v,
                false
        );
        durabilityThresholdSlider.setCustomValueKey(
                "vm.config.value.durability"
        );
        widgets.add(durabilityThresholdSlider);

        layout.next(20);

        widgets.add(new TitleWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.protected_tools")
        ));
        layout.next(16);

        protectedToolsBox = MultiLineEditBox.builder()
                .setX(layout.x())
                .setY(layout.y())
                .setShowBackground(true)
                .setShowDecorations(true)
                .build(
                        Minecraft.getInstance().font,
                        contentWidth,
                        40,
                        Component.translatable("vm.config.protected_tools")
                );

        protectedToolsBox.setValue(String.join("\n", items.protectedTools));
        protectedToolsBox.setTooltip(
                Tooltip.create(Component.translatable("vm.config.protected_tools.tooltip"))
        );

        widgets.add(protectedToolsBox);

        return widgets;
    }

    public void save() {
        items.protectedTools.clear();
        for (String line : protectedToolsBox.getValue().split("\n")) {
            if (!line.isBlank()) {
                items.protectedTools.add(line.trim());
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

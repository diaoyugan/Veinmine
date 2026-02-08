package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.configScreen.widget.*;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

//TODO:我有史以来写过最大便的坐标计算 一定要优化
public class ConfigHighlightsPage {
    private final ConfigItems items;

    public ConfigHighlightsPage(ConfigItems items) {
        this.items = items;
    }

    public List<AbstractWidget> build(int centerX) {
        List<AbstractWidget> widgets = new ArrayList<>();
        final int contentWidth = 200;  // 假设总宽度是 200
        final int sliderWidth = 90;  // 滑块宽度是总宽度的一半 预留10的间隔 算上预览起始位置应该是20
        final int sliderHeight = 20;  // 每个滑块的高度
        final int numSliders = 4;  // 颜色滑块的数量
        final int previewHeight = (sliderHeight * numSliders) + 24;  // 预览框的高度等于滑块的总高度

        final int leftX = centerX - 90;
        VerticalLayout layout = new VerticalLayout(leftX, 30, 4);

        // 标题
        widgets.add(new TitleWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.screen.highlights")
        ));
        layout.next(16);

        // Enable Highlights Checkbox
        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.enableHighlights",
                () -> items.enableHighlights,
                v -> items.enableHighlights = v
        ));
        layout.next(24);

        // 创建红色滑块
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderRed", 0, 255, items.red, v -> items.red = v,false));

        // 计算颜色预览框的位置，放在红色滑块的右边
        int previewX = layout.x() + sliderWidth;  // 预览框的 X 坐标
        AbstractWidget previewWidget = new ColorPreviewWidget(
                previewX + 20, layout.y(), sliderWidth, previewHeight,  // 设置预览框的高度
                () -> items.red, () -> items.green, () -> items.blue, () -> items.alpha
        );
        widgets.add(previewWidget);

        // 创建其他颜色的滑块
        layout.next(24);
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderGreen", 0, 255, items.green, v -> items.green = v,false));
        layout.next(24);
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderBlue", 0, 255, items.blue, v -> items.blue = v,false));
        layout.next(24);
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderAlpha", 0, 255, items.alpha, v -> items.alpha = v,true));

        return widgets;
    }



    // 创建 RGB 滑块
    private IntSliderOptionWidget createColorSlider(VerticalLayout layout, int width, String key, int min, int max, int value, IntConsumer setter,Boolean isPercentage) {
        return new IntSliderOptionWidget(
                layout.x(),
                layout.y(),
                width,
                20,
                Component.translatable(key),
                min,
                max,
                () -> value,
                setter,
                isPercentage
        );
    }

    private BooleanOptionWidget bool(VerticalLayout layout, int width, String key, BooleanSupplier getter, Consumer<Boolean> setter) {
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
}

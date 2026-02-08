package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import top.diaoyugan.veinmine.client.configScreen.widget.VerticalLayout;
import top.diaoyugan.veinmine.client.configScreen.widget.BooleanOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ConfigAdvancedPage {

    private final ConfigItems items;

    public ConfigAdvancedPage(ConfigItems items) {
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
                Component.translatable("vm.config.screen.final_resort")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
        ));
        layout.next(16);

        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.useIntrusiveCode",
                () -> items.useIntrusiveCode,
                v -> items.useIntrusiveCode = v
        ));
        return widgets;
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

}


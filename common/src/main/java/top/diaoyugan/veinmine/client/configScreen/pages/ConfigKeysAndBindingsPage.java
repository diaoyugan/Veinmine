package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.configScreen.widget.*;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ConfigKeysAndBindingsPage {

    private final ConfigItems items;

    public ConfigKeysAndBindingsPage(ConfigItems items) {
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
                Component.translatable("vm.config.screen.keysAndBinding")
        ));
        layout.next(16);

        widgets.add(Button.builder(
                Component.translatable("vm.config.open_keybindings"),
                b -> {
                    if (Minecraft.getInstance().screen != null) {
                        Minecraft.getInstance().setScreen(
                                new KeyBindsScreen(
                                        Minecraft.getInstance().screen,
                                        Minecraft.getInstance().options
                                )
                        );
                    }
                }
        ).bounds(
                layout.x(),
                layout.y(),
                contentWidth,
                20
        ).tooltip(
                Tooltip.create(Component.translatable("vm.config.open_keybindings.tooltip"))
        )
        .build());

        layout.next(20);


        widgets.add(bool(
                layout,
                contentWidth,
                "vm.config.use_hold_instead_of_toggle",
                () -> items.useHoldInsteadOfToggle,
                v -> items.useHoldInsteadOfToggle = v
        ));
        layout.next(20);

        return widgets;
    }

    public void save() {
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

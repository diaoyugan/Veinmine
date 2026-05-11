package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.KeyBinding;
import top.diaoyugan.veinmine.client.configScreen.layout.VerticalLayout;
import top.diaoyugan.veinmine.client.configScreen.widget.*;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ConfigKeysAndBindingsPage extends Screen {

    private KeyBindingButtonWidget singelBindButton;
    private CombinationKeyBindingButtonWidget bindButton;
    private final ConfigItems items;

    public ConfigKeysAndBindingsPage(ConfigItems items) {
        super(Component.translatable("vm.config.screen.keysAndBinding"));
        this.items = items;
    }

    public List<AbstractWidget> build(int centerX) {
        List<AbstractWidget> widgets = new ArrayList<>();

        final int contentWidth = 200;
        final int leftX = centerX - contentWidth / 2;

        VerticalLayout layout = new VerticalLayout(leftX, 10, 4);

        // 标题
        widgets.add(new TextWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.screen.keysAndBinding")
        ));
        layout.next(10);

        singelBindButton = (new KeyBindingButtonWidget(
                layout.x(),
                layout.y(),
                contentWidth,
                20,
                Component.translatable("key.vm.switch"),
                () -> items.keyBindingCode,
                v -> {
                    items.keyBindingCode = v;
                },
                true,
                KeyBinding.ACTIVATION_KEY
        ));
        singelBindButton.tooltip(Component.translatable("vm.config.keybinds.tooltip"));
        widgets.add(singelBindButton);

        layout.next(20);

        bindButton = (new CombinationKeyBindingButtonWidget(
                layout.x(),
                layout.y(),
                contentWidth,
                20,
                Component.translatable("vm.config.config_screen_keys"),
                () -> items.configScreenKey,
                v -> {
                    items.configScreenKey.clear();
                    items.configScreenKey.addAll(v);
                }
        ));
        bindButton.tooltip(Component.translatable("vm.config.keybinds.combination.tooltip"));
        widgets.add(bindButton);

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

    public void tick() {
        super.tick();
        bindButton.tick();
    }

    public boolean onKeyDown(KeyEvent event) {

        boolean handled = false;

        if (singelBindButton != null) {
            handled |= singelBindButton.keyPressed(event);
        }

        if (bindButton != null) {
            handled |= bindButton.keyPressed(event);
        }

        return handled;
    }
}

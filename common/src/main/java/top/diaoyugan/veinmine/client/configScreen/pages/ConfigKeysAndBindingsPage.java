package top.diaoyugan.veinmine.client.configScreen.pages;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.configScreen.layout.VerticalLayout;
import top.diaoyugan.veinmine.client.configScreen.widget.*;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfigKeysAndBindingsPage extends Screen {

    private final ConfigItems items;
    private final Set<Integer> currentKeys;
    private boolean listening = false;
    private Button bindButton;

    public ConfigKeysAndBindingsPage(ConfigItems items) {
        super(Component.translatable("vm.config.screen.keysAndBinding"));
        this.items = items;
        this.currentKeys = items.configScreenKey;
    }

    public List<AbstractWidget> build(int centerX) {
        List<AbstractWidget> widgets = new ArrayList<>();

        final int contentWidth = 200;
        final int leftX = centerX - contentWidth / 2;

        VerticalLayout layout = new VerticalLayout(leftX, 10, 4);

        // 标题
        widgets.add(new TitleWidget(
                layout.x(),
                layout.y(),
                Component.translatable("vm.config.screen.keysAndBinding")
        ));
        layout.next(10);

        widgets.add(Button.builder(
                Component.translatable("vm.config.open_keybindings"),
                b -> {
                        Minecraft.getInstance().setScreenAndShow(
                                new KeyBindsScreen(
                                        this,
                                        Minecraft.getInstance().options
                                )
                        );
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

        bindButton = Button.builder(
                formatKeyBindingText(),
                b -> {
                    listening = true;
                    currentKeys.clear();
                    refreshButtonText();
                }
        ).bounds(layout.x(), layout.y(), contentWidth, 20)
                .tooltip(
                        Tooltip.create(Component.translatable("vm.config.bind_menu_keys.tooltip"))
                )
                .build();
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
        items.configScreenKey = currentKeys;
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

        if (!listening) return;

        Window window = Minecraft.getInstance().getWindow();

        // 监听所有候选键
        for (int key : currentKeys) {

            if (!InputConstants.isKeyDown(window, key)) {
                finishBinding();
                return;
            }
        }
    }

    public boolean onKeyDown(KeyEvent event) {

        if (!listening) return false;

        if (event.key() == InputConstants.KEY_ESCAPE) {
            listening = false;
            refreshButtonText();
            return true;
        }

        currentKeys.add(event.key());
        return true;
    }

    private void finishBinding() {
        listening = false;
        refreshButtonText();
    }

    private Component formatKeyBindingText() {

        if (listening) {
            return Component.translatable("vm.config.keys.listening");
        }

        if (currentKeys.isEmpty()) {
            return Component.translatable("vm.config.bind_menu_keys.none");
        }

        String keys = currentKeys.stream()
                .map(InputConstants.Type.KEYSYM::getOrCreate)
                .map(InputConstants.Key::getDisplayName)
                .map(Component::getString)
                .collect(Collectors.joining(" + "));

        return Component.translatable("vm.config.bind_menu_keys.current", keys);
    }

    private void refreshButtonText() {
        bindButton.setMessage(formatKeyBindingText());
    }
}

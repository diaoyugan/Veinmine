package top.diaoyugan.veinmine.client.configScreen.widget;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class KeyBindingButtonWidget extends Button.Plain {
    private final Component label;
    private final Supplier<Set<Integer>> getter;
    private final Consumer<Set<Integer>> setter;

    private boolean listening = false;

    public KeyBindingButtonWidget(
            int x,
            int y,
            int width,
            int height,
            Component label,
            Supplier<Set<Integer>> getter,
            Consumer<Set<Integer>> setter
    ) {
        super(
                x,
                y,
                width,
                height,
                Component.empty(),
                b -> {},
                DEFAULT_NARRATION
        );

        this.label = label;
        this.getter = getter;
        this.setter = setter;

        refreshMessage();
    }

    public void tooltip(Component tooltip) {
        setTooltip(Tooltip.create(tooltip));
    }

    // -------------------------
    // input
    // -------------------------

    @Override
    public void onPress(InputWithModifiers input) {
        listening = true;

        Set<Integer> keys = getter.get();
        keys.clear();

        refreshMessage();
    }

    public boolean keyPressed(KeyEvent event) {
        if (!listening) {
            return false;
        }

        if (event.key() == InputConstants.KEY_ESCAPE) {
            listening = false;
            refreshMessage();
            return true;
        }

        Set<Integer> keys = getter.get();

        keys.add(event.key());
        refreshMessage();

        return true;
    }

    public void tick() {
        if (!listening) {
            return;
        }

        Window window = Minecraft.getInstance().getWindow();

        for (int key : getter.get()) {
            if (!InputConstants.isKeyDown(window, key)) {
                finishBinding();
                return;
            }
        }
    }

    // -------------------------
    // finish
    // -------------------------

    private void finishBinding() {
        listening = false;
        refreshMessage();
    }

    // -------------------------
    // UI text
    // -------------------------

    private void refreshMessage() {
        setMessage(formatMessage());
    }

    private Component formatMessage() {
        Set<Integer> keys = getter.get();

        if (listening) {
            return Component.translatable(
                    "vm.config.keybind.listening",
                    label
            );
        }

        if (keys.isEmpty()) {
            return Component.translatable(
                    "vm.config.keybind.none",
                    label
            );
        }

        String text = keys.stream()
                .map(InputConstants.Type.KEYSYM::getOrCreate)
                .map(InputConstants.Key::getDisplayName)
                .map(Component::getString)
                .collect(Collectors.joining(" + "));

        return Component.translatable(
                "vm.config.keybind.current",
                label,
                text
        );
    }

    // -------------------------
    // render / narration
    // -------------------------

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // no-op
    }
}
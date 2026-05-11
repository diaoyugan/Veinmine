package top.diaoyugan.veinmine.client.configScreen.widget;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class KeyBindingButtonWidget extends Button.Plain {
    private final Component label;
    private final IntSupplier getter;
    private final IntConsumer setter;

    private final boolean syncVanilla;
    @Nullable
    private final KeyMapping vanillaKeyMapping;

    private boolean listening = false;

    public KeyBindingButtonWidget(
            int x,
            int y,
            int width,
            int height,
            Component label,
            IntSupplier getter,
            IntConsumer setter,
            boolean syncVanilla,
            @Nullable KeyMapping vanillaKeyMapping
    ) {
        super(x, y, width, height, Component.empty(), b -> {}, DEFAULT_NARRATION);

        this.label = label;
        this.getter = getter;
        this.setter = setter;
        this.syncVanilla = syncVanilla;
        this.vanillaKeyMapping = vanillaKeyMapping;

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
        refreshMessage();
    }

    public boolean keyPressed(KeyEvent event) {
        if (!listening) return false;

        if (event.key() == InputConstants.KEY_ESCAPE) {
            listening = false;
            refreshMessage();
            return true;
        }

        setter.accept(event.key());
        finishBinding();

        return true;
    }

    // -------------------------
    // finish
    // -------------------------

    private void finishBinding() {
        listening = false;
        syncVanillaKey();
        refreshMessage();
    }

    // -------------------------
    // vanilla sync
    // -------------------------

    private void syncVanillaKey() {
        if (!syncVanilla || vanillaKeyMapping == null) return;

        int key = getter.getAsInt();

        vanillaKeyMapping.setKey(
                InputConstants.Type.KEYSYM.getOrCreate(key)
        );
    }

    // -------------------------
    // UI
    // -------------------------

    private void refreshMessage() {
        int key = getter.getAsInt();

        if (listening) {
            setMessage(Component.translatable("vm.config.keybind.listening", label));
            return;
        }

        String text = InputConstants.Type.KEYSYM.getOrCreate(key)
                .getDisplayName()
                .getString();

        setMessage(Component.translatable("vm.config.keybind.current", label, text));
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
}
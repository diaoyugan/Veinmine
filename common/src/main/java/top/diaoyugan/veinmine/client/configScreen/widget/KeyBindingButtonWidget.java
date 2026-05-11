package top.diaoyugan.veinmine.client.configScreen.widget;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.client.input.KeyEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeyBindingButtonWidget extends Button.Plain {

    private final Component label;

    /**
     * 保存按键结果
     */
    private final Consumer<InputConstants.Key> setter;

    /**
     * 当前按键显示文本来源
     */
    @Nullable
    private final Supplier<Component> displaySupplier;

    /**
     * 可选 vanilla key mapping
     */
    @Nullable
    private final KeyMapping vanillaKeyMapping;

    private boolean listening = false;
    private final boolean syncVanilla;

    public KeyBindingButtonWidget(
            int x,
            int y,
            int width,
            int height,
            Component label,
            Consumer<InputConstants.Key> setter,
            @Nullable Supplier<Component> displaySupplier,
            @Nullable KeyMapping vanillaKeyMapping,
            boolean syncVanilla
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
        this.setter = setter;
        this.displaySupplier = displaySupplier;
        this.vanillaKeyMapping = vanillaKeyMapping;
        this.syncVanilla = syncVanilla;

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
        if (!listening) {
            return false;
        }

        InputConstants.Key key;

        if (event.key() == InputConstants.KEY_ESCAPE) {
            key = InputConstants.UNKNOWN;
        } else {
            key = InputConstants.Type.KEYSYM.getOrCreate(event.key());
        }

        setter.accept(key);

        syncVanillaKey(key);

        listening = false;
        refreshMessage();

        return true;
    }

    // -------------------------
    // sync vanilla
    // -------------------------

    private void syncVanillaKey(InputConstants.Key key) {
        if (!syncVanilla || vanillaKeyMapping == null) {
            return;
        }

        vanillaKeyMapping.setKey(key);

        KeyMapping.resetMapping();
    }

    // -------------------------
    // UI
    // -------------------------

    public void refreshMessage() {
        if (listening) {
            setMessage(Component.translatable(
                    "vm.config.keybind.listening",
                    label
            ));
            return;
        }

        Component keyName;

        if (displaySupplier != null) {
            keyName = displaySupplier.get();
        } else if (vanillaKeyMapping != null) {
            keyName = vanillaKeyMapping.getTranslatedKeyMessage();
        } else {
            keyName = Component.translatable("key.keyboard.unknown");
        }

        setMessage(Component.translatable(
                "vm.config.keybind.current",
                label,
                keyName
        ));
    }

    @Override
    public void updateWidgetNarration(
            NarrationElementOutput narrationElementOutput
    ) {
    }
}
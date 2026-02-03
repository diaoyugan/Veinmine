package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Function;

public class IntSliderOptionWidget extends AbstractSliderButton {

    private final int min;
    private final int max;
    private final IntSupplier getter;
    private final IntConsumer setter;
    private final Function<Integer, Component> valueText;

    public IntSliderOptionWidget(
            int x, int y, int width, int height,
            Component label,
            int min, int max,
            IntSupplier getter,
            IntConsumer setter,
            Function<Integer, Component> valueText
    ) {
        super(
                x, y, width, height,
                label,
                (getter.getAsInt() - min) / (double) (max - min)
        );
        this.min = min;
        this.max = max;
        this.getter = getter;
        this.setter = setter;
        this.valueText = valueText;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        int value = getValue();
        setMessage(
                Component.empty()
                        .append(super.getMessage())
                        .append(": ")
                        .append(valueText.apply(value))
        );
    }

    @Override
    protected void applyValue() {
        setter.accept(getValue());
    }

    private int getValue() {
        return min + (int) Math.round(value * (max - min));
    }
}

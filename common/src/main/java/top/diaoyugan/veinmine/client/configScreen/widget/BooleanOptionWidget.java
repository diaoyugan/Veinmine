package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class BooleanOptionWidget extends BaseOptionWidget {

    private final BooleanSupplier getter;
    private final Consumer<Boolean> setter;
    private Component tooltip;
    private boolean isHalfWidth = false;

    public BooleanOptionWidget(
            int x, int y, int width, int height,
            Component label,
            BooleanSupplier getter,
            Consumer<Boolean> setter
    ) {
        super(x, y, width, height, label);
        this.getter = getter;
        this.setter = setter;
    }

    public void setHalfWidth(){
        isHalfWidth = true;
    }

    public void tooltip(Component tooltip) {
        setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void renderContent(
            GuiGraphicsExtractor g,
            int x, int y,
            int width, int height,
            int mouseX, int mouseY
    ) {
        Minecraft mc = Minecraft.getInstance();
        boolean value = getter.getAsBoolean();

        g.text(
                mc.font,
                getMessage(),
                x + 6,
                y + 6,
                0xFFFFFFFF,
                false
        );

        Component state = value
                ? Component.translatable("options.on")
                : Component.translatable("options.off");
        if (!isHalfWidth){
            g.text(
                    mc.font,
                    state,
                    x + width - 40,
                    y + 6,
                    value ? 0xFF55FF55 : 0xFFFF5555,
                    false
            );
        }else{
            g.text(
                    mc.font,
                    state,
                    x + width - 20,
                    y + 6,
                    value ? 0xFF55FF55 : 0xFFFF5555,
                    false
            );
        }
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        setter.accept(!getter.getAsBoolean());
    }
}

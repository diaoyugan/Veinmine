package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

// 被过去的自己拯救了 原ColorPreviewEntry
// 创建颜色预览控件
public class ColorPreviewWidget extends AbstractWidget {
    private final Supplier<Integer> red;
    private final Supplier<Integer> green;
    private final Supplier<Integer> blue;
    private final Supplier<Integer> alpha;

    public ColorPreviewWidget(int x, int y, int width, int height,
                              Supplier<Integer> red, Supplier<Integer> green,
                              Supplier<Integer> blue, Supplier<Integer> alpha) {
        super(x, y, width, height, Component.translatable("vm.config.color_preview"));
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int color = 0xFF000000 | (red.get() << 16) | (green.get() << 8) | blue.get() | (alpha.get() << 24);
        graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}


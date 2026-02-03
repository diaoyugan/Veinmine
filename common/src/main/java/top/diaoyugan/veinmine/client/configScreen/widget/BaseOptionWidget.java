package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class BaseOptionWidget extends AbstractWidget {

    protected BaseOptionWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        int bg = isHoveredOrFocused() ? 0xFF555555 : 0xFF333333;
        g.fill(x, y, x + w, y + h, bg);

        renderContent(g, x, y ,w ,h , mouseX, mouseY);
    }

    protected abstract void renderContent(
            GuiGraphics g,
            int x, int y,
            int width, int height,
            int mouseX, int mouseY
    );

}

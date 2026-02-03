package top.diaoyugan.veinmine.client.configScreen.elements;

import net.minecraft.client.gui.GuiGraphics;

public abstract class UIComponent {
    protected int x, y, width, height;

    protected UIComponent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // 由容器自动设置位置
    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(GuiGraphics g, int mouseX, int mouseY, float delta);

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean isHovered(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}

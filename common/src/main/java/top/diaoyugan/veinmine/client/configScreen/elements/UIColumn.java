package top.diaoyugan.veinmine.client.configScreen.elements;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class UIColumn {

    private final List<UIComponent> children = new ArrayList<>();
    private final int x, y, width, height;
    private int scroll;
    private final int spacing = 6;

    public UIColumn(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void add(UIComponent c) {
        children.add(c);
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int cy = y - scroll;

        for (UIComponent c : children) {
            c.setPosition(x, cy);
            c.render(g, mouseX, mouseY, delta);
            cy += c.height + spacing;
        }
    }

    public boolean mouseClicked(double mx, double my, int btn) {
        for (UIComponent c : children) {
            if (c.mouseClicked(mx, my, btn)) return true;
        }
        return false;
    }

    public void scroll(int amount) {
        scroll = Math.max(0, scroll + amount);
    }
}

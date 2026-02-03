package top.diaoyugan.veinmine.client.configScreen.elements;

import net.minecraft.client.gui.GuiGraphics;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class UISlider extends UIComponent {

    private final int min, max;
    private final IntSupplier getter;
    private final IntConsumer setter;
    private boolean dragging;

    public UISlider(int width, int height, int min, int max,
                    IntSupplier getter, IntConsumer setter) {
        super(width, height);
        this.min = min;
        this.max = max;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int cy = y + height / 2;

        g.fill(x, cy - 1, x + width, cy + 1, 0xFF888888);

        float t = (getter.getAsInt() - min) / (float) (max - min);
        int knobX = x + (int) (t * (width - 8));

        g.fill(knobX, y, knobX + 8, y + height, 0xFFBBBBBB);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY)) {
            dragging = true;
            update(mouseX);
            return true;
        }
        return false;
    }

    public void mouseReleased() {
        dragging = false;
    }

    public void mouseDragged(double mouseX) {
        if (dragging) update(mouseX);
    }

    private void update(double mouseX) {
        float t = (float) ((mouseX - x) / (width - 8));
        t = Math.max(0, Math.min(1, t));
        setter.accept(min + Math.round(t * (max - min)));
    }
}

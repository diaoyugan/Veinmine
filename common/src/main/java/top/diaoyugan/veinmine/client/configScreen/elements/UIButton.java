package top.diaoyugan.veinmine.client.configScreen.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class UIButton extends UIComponent {

    private final Component label;
    private final Runnable onClick;

    public UIButton(int width, int height, Component label, Runnable onClick) {
        super(width, height);
        this.label = label;
        this.onClick = onClick;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int bg = isHovered(mouseX, mouseY) ? 0xFF666666 : 0xFF444444;
        g.fill(x, y, x + width, y + height, bg);

        int textX = x + (width - g.guiWidth()) / 2;
        int textY = y + (height - 8) / 2;
        g.drawString(Minecraft.getInstance().font, label, textX, textY, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY)) {
            onClick.run();
            return true;
        }
        return false;
    }
}

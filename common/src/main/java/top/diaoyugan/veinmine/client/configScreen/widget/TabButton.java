package top.diaoyugan.veinmine.client.configScreen.widget;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TabButton extends Button {
    public TabButton(int x, int y, int width, int height,
                     Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractDefaultSprite(graphics);
        this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
    }
    @Override
    protected void handleCursor(GuiGraphicsExtractor graphics) {
        if (this.isHovered()) {
            if (this.active) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            } else {
                graphics.requestCursor(CursorTypes.ARROW);
            }
        }
    }
}

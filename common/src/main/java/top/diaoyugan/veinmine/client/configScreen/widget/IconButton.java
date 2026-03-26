package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class IconButton extends Button.Plain {

    private final Identifier iconTexture;
    private final int iconTexW;
    private final int iconTexH;
    private final float u;
    private final float v;

    public IconButton(int x, int y, int width, int height,
                      Identifier iconTexture,
                      int iconTexW, int iconTexH,
                      float u, float v,
                      OnPress onPress) {

        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);

        this.iconTexture = iconTexture;
        this.iconTexW = iconTexW;
        this.iconTexH = iconTexH;
        this.u = u;
        this.v = v;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {

        super.extractContents(g, mouseX, mouseY, partialTick);

        int iconX = this.getX() + (this.width - 16) / 2;
        int iconY = this.getY() + (this.height - 16) / 2;

        g.blit(
                RenderPipelines.GUI_TEXTURED,
                iconTexture,
                iconX,
                iconY,
                u,
                v,
                16,
                16,
                iconTexW,
                iconTexH
        );
    }
}
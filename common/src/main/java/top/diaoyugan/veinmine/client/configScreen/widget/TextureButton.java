package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class TextureButton extends Button {

    private final Identifier textureLocation;
    private final int textureWidth;
    private final int textureHeight;
    private final Tooltip tooltip;
    private final Identifier hoverTexture;
    private final int hoverTextureWidth;
    private final int hoverTextureHeight;
    private final boolean useSeparateHoverTexture;

    private final float u;
    private final float v;
    private final float hoverVShift;

    public TextureButton(int x,
                         int y,
                         int width,
                         int height,
                         Identifier textureLocation,
                         int textureWidth,
                         int textureHeight,
                         Identifier hoverTextureLocation,
                         int hoverTextureWidth,
                         int hovertextureHeight,
                         float u,
                         float v,
                         float hoverVShift,
                         OnPress onPress,
                         Tooltip tooltip)
    {

        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);

        this.tooltip = tooltip;

        if (tooltip != null) {
            this.setTooltip(tooltip);
        }
        this.textureLocation = textureLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.hoverVShift = hoverVShift;
        this.hoverTexture = hoverTextureLocation;
        this.hoverTextureWidth = hoverTextureWidth;
        this.hoverTextureHeight = hovertextureHeight;
        this.useSeparateHoverTexture = hoverTexture != null;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {

        Identifier textureToUse = this.textureLocation;
        float drawV = this.v;

        int texW = this.textureWidth;
        int texH = this.textureHeight;

        if (this.isHovered()) {
            if (useSeparateHoverTexture) {
                textureToUse = hoverTexture;

                texW = hoverTextureWidth;
                texH = hoverTextureHeight;
            } else {
                drawV += hoverVShift;
            }
        }

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                textureToUse,
                this.getX(),
                this.getY(),
                u,
                drawV,
                this.width,
                this.height,
                texW,
                texH
        );
    }

    /**
     Builder
     **/
    public static class Builder {

        private final int x, y, width, height;

        private Identifier textureLocation;
        private int textureWidth;
        private int textureHeight;
        private int hoverTextureWidth;
        private int hoverTextureHeight;

        private float u;
        private float v;
        private float hoverVShift = 0;

        private OnPress onPress = b -> {};
        private Tooltip tooltip;
        private Identifier hoverTexture;

        public Builder(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Builder texture(Identifier texture, int texW, int texH) {
            this.textureLocation = texture;
            this.textureWidth = texW;
            this.textureHeight = texH;
            return this;
        }

        public Builder hoverTexture(Identifier texture,int texW, int texH) {
            this.hoverTexture = texture;
            this.hoverTextureWidth = texW;
            this.hoverTextureHeight = texH;
            return this;
        }

        public Builder uv(float u, float v) {
            this.u = u;
            this.v = v;
            return this;
        }

        public Builder hoverShift(float shift) {
            this.hoverVShift = shift;
            return this;
        }

        public Builder onPress(OnPress onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder tooltip(Component text) {
            this.tooltip = Tooltip.create(text);
            return this;
        }

        public TextureButton build() {
            if (textureLocation == null) {
                throw new IllegalStateException("Texture not set");
            }

            return new TextureButton(
                    x, y, width, height,
                    textureLocation,
                    textureWidth,
                    textureHeight,
                    hoverTexture,
                    hoverTextureWidth,
                    hoverTextureHeight,
                    u, v,
                    hoverVShift,
                    onPress,
                    tooltip
            );
        }
    }
}
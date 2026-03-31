package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import top.diaoyugan.veinmine.client.configScreen.widgetSprites.EnhancedWidgetSprites;
import top.diaoyugan.veinmine.client.configScreen.widgetSprites.SpriteData;

public class TextureButton extends Button {

    private final EnhancedWidgetSprites enhancedSprites;
    private final Tooltip tooltip;

    public TextureButton(int x,
                         int y,
                         int width,
                         int height,
                         EnhancedWidgetSprites enhancedSprites,
                         OnPress onPress,
                         Tooltip tooltip) {

        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);

        this.enhancedSprites = enhancedSprites;
        this.tooltip = tooltip;

        if (tooltip != null) {
            this.setTooltip(tooltip);
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    /* ---------- 渲染 ---------- */

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {

        SpriteData sprite = enhancedSprites.get(this.active, this.isHoveredOrFocused());

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                sprite.texture(),
                this.getX(),
                this.getY(),
                sprite.u(),
                sprite.v(),
                this.width,
                this.height,
                sprite.texW(),
                sprite.texH()
        );
    }

    /* ---------- Builder ---------- */

    public static class Builder {

        private final int x, y, width, height;

        private Identifier texture;
        private int texW, texH;

        private Identifier hoverTexture;
        private int hoverTexW, hoverTexH;

        private float u, v;
        private float hoverVShift = 0;

        private OnPress onPress = b -> {};
        private Tooltip tooltip;

        public Builder(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Builder texture(Identifier texture, int texW, int texH) {
            this.texture = texture;
            this.texW = texW;
            this.texH = texH;
            return this;
        }

        public Builder hoverTexture(Identifier texture, int texW, int texH) {
            this.hoverTexture = texture;
            this.hoverTexW = texW;
            this.hoverTexH = texH;
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

            if (texture == null) {
                throw new IllegalStateException("Texture not set");
            }

            SpriteData normal = new SpriteData(texture, u, v, texW, texH);

            SpriteData hovered;

            if (hoverTexture != null) {
                hovered = new SpriteData(hoverTexture, u, v, hoverTexW, hoverTexH);
            } else {
                hovered = new SpriteData(texture, u, v + hoverVShift, texW, texH);
            }


            EnhancedWidgetSprites sprites = new EnhancedWidgetSprites(normal)
                    .with(WidgetState.HOVERED, hovered);

            return new TextureButton(
                    x, y, width, height,
                    sprites,
                    onPress,
                    tooltip
            );
        }
    }
}
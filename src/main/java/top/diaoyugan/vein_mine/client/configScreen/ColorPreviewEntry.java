package top.diaoyugan.vein_mine.client.configScreen;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ColorPreviewEntry extends AbstractConfigListEntry<Void> {
    private final Supplier<Integer> red;
    private final Supplier<Integer> green;
    private final Supplier<Integer> blue;

    public ColorPreviewEntry(Supplier<Integer> red, Supplier<Integer> green, Supplier<Integer> blue) {
        super(Text.literal("Color Preview"), false);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    // 小型渲染参数封装
    private static class RenderParams {
        int index, x, y, width, height, mouseX, mouseY;
        boolean selected;
        float delta;

        RenderParams(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean selected, float delta) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.selected = selected;
            this.delta = delta;
        }
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                       int mouseX, int mouseY, boolean selected, float delta) {
        RenderParams params = new RenderParams(index, x, y, entryWidth, entryHeight, mouseX, mouseY, selected, delta);
        renderColorPreview(context, params);
    }

    // 拆分实际渲染逻辑
    private void renderColorPreview(DrawContext context, RenderParams p) {
        super.render(context, p.index, p.y, p.x, p.width, p.height, p.mouseX, p.mouseY, p.selected, p.delta);

        int color = 0xFF000000 | (red.get() << 16) | (green.get() << 8) | blue.get();

        // 渲染文字
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.translatable("vm.config.color_preview"),
                p.x + 5, p.y + 6,
                0xFFFFFFFF,
                false
        );

        // 小方块渲染参数，完美对齐
        int margin = 27;
        int boxWidth = 124;
        int boxHeight = 20;
        int boxX = p.x + p.width - boxWidth - margin;
        int boxY = p.y;

        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, color);
    }

    @Override
    public Void getValue() { return null; }

    @Override
    public Optional<Void> getDefaultValue() { return Optional.empty(); }

    @Override
    public boolean isEdited() { return false; }

    @Override
    public List<? extends Selectable> narratables() { return List.of(); }

    @Override
    public List<? extends Element> children() { return List.of(); }
}

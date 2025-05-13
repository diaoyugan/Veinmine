package top.diaoyugan.vein_mine.client.ConfigScreen;

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
        super(Text.literal("Color Preview"),false);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                       int mouseX, int mouseY, boolean selected, float delta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, selected, delta);

        int color = 0xFF000000 | (red.get() << 16) | (green.get() << 8) | blue.get();

        // 文字渲染
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.translatable("vm.config.color_preview"),
                x + 5, y + 6,
                0xFFFFFF,
                false
        );

        // 小方块渲染参数 此处是精确计算的完美对齐 除非你知道你在干什么 不然别动！
        int margin = 27;
        int boxWidth = 124;
        int boxHeight = 20;
        // 自动靠右对齐（减去颜色条宽度和边距）
        int boxX = x + entryWidth - boxWidth - margin;
        int boxY = y + 0;

        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, color); // 实际颜色预览
    }

    @Override public Void getValue() { return null; }

    @Override
    public Optional<Void> getDefaultValue() {
        return Optional.empty();
    }

    //   @Override public void setValue(Void value) {}
    @Override public boolean isEdited() { return false; }

    @Override
    public List<? extends Selectable> narratables() {
        return List.of();
    }

    @Override
    public List<? extends Element> children() {
        return List.of();
    }
}

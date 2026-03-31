package top.diaoyugan.veinmine.client.configScreen.widgetSprites;

import top.diaoyugan.veinmine.client.configScreen.widget.WidgetState;

import java.util.EnumMap;

public class EnhancedWidgetSprites {

    private final EnumMap<WidgetState, SpriteData> sprites;

    public EnhancedWidgetSprites(SpriteData normal) {
        this.sprites = new EnumMap<>(WidgetState.class);
        this.sprites.put(WidgetState.NORMAL, normal);
    }

    public EnhancedWidgetSprites with(WidgetState state, SpriteData data) {
        this.sprites.put(state, data);
        return this;
    }

    public SpriteData get(boolean active, boolean hovered) {
        if (!active) {
            return sprites.getOrDefault(WidgetState.DISABLED, sprites.get(WidgetState.NORMAL));
        }
        if (hovered) {
            return sprites.getOrDefault(WidgetState.HOVERED, sprites.get(WidgetState.NORMAL));
        }
        return sprites.get(WidgetState.NORMAL);
    }
}

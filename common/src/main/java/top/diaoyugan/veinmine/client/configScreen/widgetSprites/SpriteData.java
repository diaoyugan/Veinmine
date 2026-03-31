package top.diaoyugan.veinmine.client.configScreen.widgetSprites;

import net.minecraft.resources.Identifier;

public record SpriteData(
        Identifier texture,
        float u,
        float v,
        int texW,
        int texH
) {}

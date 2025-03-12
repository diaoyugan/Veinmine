package top.diaoyugan.vein_mine;

import java.util.HashSet;
import java.util.Set;

public class ConfigItems {
    private final int version = 1; // Config file version, modify after config format change, not used now

    public int searchRadius = 1;
    public int BFSLimit = 50;
    public Set<String> ignoredBlocks = new HashSet<>();
    public boolean useBFS = true;
    public boolean useRadiusSearch = true;
    public boolean useRadiusSearchWhenReachBFSLimit = true;
    public boolean protectTools = true;
    public Set<String> protectedTools = new HashSet<>();
    public int durabilityThreshold = 10;
    public boolean protectAllDefaultValuableTools = true;
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int alpha = 255;
    public float thickness = 0.00f;
    public int renderTime = 1;
    public Set<String> defaultProtectedTools = Set.of( // No GUI for this, this is by design!
        // 黄金工具
        "minecraft:golden_pickaxe",
        "minecraft:golden_axe",
        "minecraft:golden_shovel",
        "minecraft:golden_sword",
        "minecraft:golden_hoe",
        // 钻石工具
        "minecraft:diamond_pickaxe",
        "minecraft:diamond_axe",
        "minecraft:diamond_shovel",
        "minecraft:diamond_sword",
        "minecraft:diamond_hoe",
        // 下界合金工具
        "minecraft:netherite_pickaxe",
        "minecraft:netherite_axe",
        "minecraft:netherite_shovel",
        "minecraft:netherite_sword",
        "minecraft:netherite_hoe"
    );
}

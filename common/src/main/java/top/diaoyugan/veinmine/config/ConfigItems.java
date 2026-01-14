package top.diaoyugan.veinmine.config;

import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class ConfigItems { // Default configuration
    private final int version = 1; // Config file version, modify after config format change, not used now

    public boolean useIntrusiveCode = true;
    public int searchRadius = 1;
    public int BFSLimit = 50;
    public Set<String> ignoredBlocks = new HashSet<>();
    public boolean useBFS = true;
    public boolean useRadiusSearch = true;
    public boolean useRadiusSearchWhenReachBFSLimit = true;
    public boolean highlightBlocksMessage = true;
    public boolean protectTools = true;
    public Set<String> protectedTools = new HashSet<>();
    public int durabilityThreshold = 10;
    public boolean protectAllDefaultValuableTools = true;
    public boolean enableHighlights = true;
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int alpha = 255;
    public int renderTime = 1;
    public boolean useHoldInsteadOfToggle = false;
    public int keyBindingCode = GLFW.GLFW_KEY_GRAVE_ACCENT; // 默认是 ~ 键
    public final Set<String> defaultProtectedTools = Set.of( // No GUI for this, this is by design!
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

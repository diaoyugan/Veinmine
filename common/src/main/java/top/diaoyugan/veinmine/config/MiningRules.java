package top.diaoyugan.veinmine.config;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record MiningRules(
        boolean protectTools,
        boolean protectDefaultTools,
        int durabilityThreshold,
        Set<String> protectedTools,
        Set<String> ignoredBlocks
) {
    private static final Set<String> DEFAULT_PROTECTED_TOOLS = Set.of(
            "minecraft:golden_pickaxe",
            "minecraft:golden_axe",
            "minecraft:golden_shovel",
            "minecraft:golden_sword",
            "minecraft:golden_hoe",
            "minecraft:diamond_pickaxe",
            "minecraft:diamond_axe",
            "minecraft:diamond_shovel",
            "minecraft:diamond_sword",
            "minecraft:diamond_hoe",
            "minecraft:netherite_pickaxe",
            "minecraft:netherite_axe",
            "minecraft:netherite_shovel",
            "minecraft:netherite_sword",
            "minecraft:netherite_hoe"
    );

    public MiningRules {
        protectedTools = snapshot(protectedTools);
        ignoredBlocks = snapshot(ignoredBlocks);
    }

    private static Set<String> snapshot(Set<String> ids) {
        return ids == null ? Set.of() : ids.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }

    public static MiningRules from(ConfigItems config) {
        return new MiningRules(config.protectTools, config.protectAllDefaultValuableTools,
                Math.max(0, config.durabilityThreshold), config.protectedTools, config.ignoredBlocks);
    }

    public int requiredDurability(String toolId) {
        if (protectTools && (protectedTools.contains(toolId)
                || protectDefaultTools && DEFAULT_PROTECTED_TOOLS.contains(toolId))) {
            return durabilityThreshold;
        }
        return -1;
    }
}

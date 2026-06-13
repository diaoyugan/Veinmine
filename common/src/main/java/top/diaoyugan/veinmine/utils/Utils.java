package top.diaoyugan.veinmine.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Utils {
    private static final Map<UUID, Boolean> playerVeinMineSwitchState = new HashMap<>();

    public static ConfigItems getConfig() {
        return Config.getInstance().getConfigItems();
    }

    public static boolean toggleVeinMineSwitchState(Player player) {
        UUID playerId = player.getUUID();
        boolean newState = !playerVeinMineSwitchState.getOrDefault(playerId, false);
        playerVeinMineSwitchState.put(playerId, newState);
        return newState;
    }

    public static boolean getVeinMineSwitchState(Player player) {
        return playerVeinMineSwitchState.getOrDefault(player.getUUID(), false);
    }

    public static void clearVeinMineState(UUID playerId) {
        playerVeinMineSwitchState.remove(playerId);
    }

    public static int calculateTotalDurabilityCost(
            List<BlockPos> blocksToBreak,
            BlockPos centerPos,
            Player player
    ) {
        Tool tool = player.getMainHandItem().get(DataComponents.TOOL);
        if (tool == null || tool.damagePerBlock() <= 0) return 0;

        int cost = 0;
        for (BlockPos targetPos : blocksToBreak) {
            if (targetPos.equals(centerPos)) continue;

            BlockState targetState = player.level().getBlockState(targetPos);
            if (targetState.getDestroySpeed(player.level(), targetPos) != 0.0F) {
                cost += tool.damagePerBlock();
            }
        }
        return cost;
    }

    public static boolean hasEnoughDurability(Player player, int cost) {
        ItemStack tool = player.getMainHandItem();
        if (isToolProtected(tool)) {
            return tool.getMaxDamage() - tool.getDamageValue() - getConfig().durabilityThreshold >= cost;
        }
        return true;
    }

    public static boolean isToolProtected(ItemStack tool) {
        String toolName = tool.getItem().toString();
        if (getConfig().protectAllDefaultValuableTools) {
            return getConfig().defaultProtectedTools.contains(toolName)
                    || getConfig().protectedTools.contains(toolName);
        }
        return getConfig().protectedTools.contains(toolName);
    }
}

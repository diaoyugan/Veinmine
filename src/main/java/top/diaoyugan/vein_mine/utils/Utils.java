package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.SVInterfaceOverride;
import top.diaoyugan.vein_mine.config.Config;
import top.diaoyugan.vein_mine.config.ConfigItems;

import java.util.*;

/**
 * 工具类，提供连锁挖掘相关的通用方法。
 */
public class Utils {
    static ServerVersionInterface SVI = new SVInterfaceOverride();
    // 创建一个Map来存储每个玩家的开关状态
    private static final Map<UUID, Boolean> playerVeinMineSwitchState = new HashMap<>();

    /**
     * 获取配置对象。
     *
     * @return 当前配置项 {@link ConfigItems}
     */
    public static ConfigItems getConfig() {
        return Config.getInstance().getConfigItems();
    }

    /**
     * 切换指定玩家的连锁挖掘开关状态。
     *
     * @param player 目标玩家
     * @return 切换后的新状态，true 表示开启，false 表示关闭
     */
    public static boolean toggleVeinMineSwitchState(PlayerEntity player) {
        UUID playerId = player.getUuid();
        boolean newState = !playerVeinMineSwitchState.getOrDefault(playerId, false);
        playerVeinMineSwitchState.put(playerId, newState);
        return newState;
    }

    /**
     * 获取指定玩家的连锁挖掘开关状态。
     *
     * @param player 目标玩家
     * @return true 表示开启，false 表示关闭
     */
    public static boolean getVeinMineSwitchState(PlayerEntity player) {
        return playerVeinMineSwitchState.getOrDefault(player.getUuid(), false);
    }

    /**
     * 清理断开连接玩家的状态，避免内存泄露。
     *
     * @param playerId 玩家 UUID
     */
    public static void clearVeinMineState(UUID playerId) {
        playerVeinMineSwitchState.remove(playerId);
    }

    /**
     * 检查玩家的主手工具是否适合挖掘指定方块。
     *
     * @param blockState 方块状态
     * @param player     玩家
     * @return true 表示工具适合挖掘，false 表示不适合
     */
    public static boolean isToolSuitable(BlockState blockState, PlayerEntity player) {
        ItemStack tool = player.getMainHandStack();
        if (blockState.isToolRequired()) {
            return tool.isSuitableFor(blockState);
        }
        return true;
    }

    /**
     * 按照破坏方块数量对玩家工具进行耐久扣除。
     *
     * @param player     玩家
     * @param blockCount 被破坏的方块数量
     */
    public static void applyToolDurabilityDamage(PlayerEntity player, int blockCount) {
        ItemStack tool = player.getMainHandStack();
        if (tool == null) return;

        if (tool.isDamageable()) {
            // 判断使用的是主手还是副手
            Hand usedHand = Hand.MAIN_HAND;
            if (player.getStackInHand(Hand.OFF_HAND) == tool) {
                usedHand = Hand.OFF_HAND;
            }
            EquipmentSlot slot = (usedHand == Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

            tool.damage(blockCount, player, slot);

            if (tool.isEmpty()) {
                player.setStackInHand(usedHand, ItemStack.EMPTY);
            }
        }
    }

    /**
     * 判断方块是否不会掉落物品。
     *
     * @param state 方块状态
     * @param world 世界
     * @param pos   方块位置
     * @return true 表示不会掉落物品，false 表示会掉落
     */
    public static boolean shouldNotDropItem(BlockState state, World world, BlockPos pos) {
        return Block.getDroppedStacks(state, (ServerWorld) world, pos, null).isEmpty();
    }

    /**
     * 计算目标方块的总耐久度消耗。
     *
     * @param blocksToBreak 需要破坏的方块位置集合
     * @param player        玩家
     * @param state         起始方块状态
     * @return 耐久消耗总和
     */
    public static int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state) {
        return SVI.calculateTotalDurabilityCost(blocksToBreak, player, state);
    }

    /**
     * 检查玩家工具的耐久度是否足够支持操作。
     *
     * @param player 玩家
     * @param cost   本次操作需要消耗的耐久值
     * @return true 表示足够，false 表示不足
     */
    public static boolean hasEnoughDurability(PlayerEntity player, int cost) {
        ItemStack tool = player.getMainHandStack();
        if (isToolProtected(tool)) {
            if (tool.getItem() != null) {
                return tool.getMaxDamage() - tool.getDamage() - getConfig().durabilityThreshold >= cost;
            }
        }
        return true;
    }

    /**
     * 检查指定工具是否在保护列表中（即需要耐久保护）。
     *
     * @param tool 工具物品堆
     * @return true 表示受保护，false 表示不受保护
     */
    public static boolean isToolProtected(ItemStack tool) {
        String toolName = tool.getItem().toString();
        if (getConfig().protectAllDefaultValuableTools) {
            return getConfig().defaultProtectedTools.contains(toolName) || getConfig().protectedTools.contains(toolName);
        }
        return getConfig().protectedTools.contains(toolName);
    }
}
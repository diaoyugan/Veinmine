package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.Config;
import top.diaoyugan.vein_mine.ConfigItems;

import java.util.*;

public class Utils {
    static ConfigItems config = new Config().getConfigItems();
    public static int searchRadius = config.searchRadius; // 搜索半径，1表示上下左右斜对角的8个方块，再加上中心方块
    public static int bfsLimit = config.BFSLimit;// 连锁搜索最大数量 超过就使用普通搜索
    public static int durabilityThreshold = config.durabilityThreshold;
    private static final Set<String> PROTECTED_TOOLS = config.protectedTools;
    public static boolean protectAllValuableTools = config.protectAllValuableTools;

    // 创建一个Map来存储每个玩家的开关状态
    private static final Map<UUID, Boolean> playerVeinMineSwitchState = new HashMap<>();

    // 切换特定玩家的开关状态
    public static boolean toggleVeinMineSwitchState(PlayerEntity player) {
        UUID playerId = player.getUuid();
        boolean newState = !playerVeinMineSwitchState.getOrDefault(playerId, false);
        playerVeinMineSwitchState.put(playerId, newState);
        return newState;
    }

    // 获取特定玩家的当前开关状态
    public static boolean getVeinMineSwitchState(PlayerEntity player) {
        return playerVeinMineSwitchState.getOrDefault(player.getUuid(), false);
    }

    public static boolean isToolSuitable(BlockState blockState, PlayerEntity player){
        ItemStack tool = player.getMainHandStack();
        if (blockState.isToolRequired()){
            return tool.isSuitableFor(blockState);
        }
        return true;
    }

    public static void applyToolDurabilityDamage(PlayerEntity player, int blockCount) {
        // 获取玩家主手工具
        ItemStack tool = player.getMainHandStack();

        // 检查工具是否能被损耗
        if (tool.isDamageable()) {
            // 按照破坏的方块数量扣除耐久
            tool.damage(blockCount, player, null);
        }
    }

    public static boolean isSilktouch(PlayerEntity player) {
        ItemStack tool = player.getMainHandStack();
        // 检查工具是否具有精准采集的附魔
        return tool.getEnchantments().toString().contains("minecraft:silk_touch");
    }

    //这是没有凋落物的方块 不要让他掉东西
    public static boolean shouldNotDropItem(BlockState state, World world, BlockPos pos){ // 判断方块是否应该掉落物品
        state.getBlock();
        return Block.getDroppedStacks(state, (ServerWorld) world, pos, null).isEmpty();
    }

    public static boolean isContainer(BlockState state) {
        return state.hasBlockEntity();
    }

    // 计算连锁挖掘的总耐久度消耗
    public static int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state) {
        int cost = 0;
        for (BlockPos targetPos : blocksToBreak) {
            BlockState targetState = player.getWorld().getBlockState(targetPos);
            if (Utils.isToolSuitable(targetState, player)) {
                cost++; // 每破坏一个适合的方块消耗1点耐久
            }
        }
        return cost;
    }
    // 检查玩家工具的耐久度是否足够
    public static boolean hasEnoughDurability(PlayerEntity player, int cost) {
        ItemStack tool = player.getMainHandStack();
        // 只有指定工具才进行耐久保护
        if (isToolProtected(tool)) {
            if (tool.getItem() != null) {
                return tool.getMaxDamage() - tool.getDamage() - durabilityThreshold >= cost;
            }
        }
        // 如果不是指定的工具，或者工具不是工具类型，就不进行保护
        return true;
    }
    // 检查玩家工具是否需要保护
    public static boolean isToolProtected(ItemStack tool) {
        String toolName = tool.getItem().toString(); // 获取工具的名称（例如 "minecraft:diamond_pickaxe"）
        if(protectAllValuableTools) {
            Set<String> ALL_VALUABLE_TOOLS = Set.of(
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
            PROTECTED_TOOLS.addAll(ALL_VALUABLE_TOOLS);
        }
        return PROTECTED_TOOLS.contains(toolName); // 如果名称在保护列表中，返回 true
    }
}


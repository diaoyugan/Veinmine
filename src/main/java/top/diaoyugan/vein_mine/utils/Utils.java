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
import top.diaoyugan.vein_mine.Config;
import top.diaoyugan.vein_mine.ConfigItems;

import java.util.*;

public class Utils {
    // 创建一个Map来存储每个玩家的开关状态
    private static final Map<UUID, Boolean> playerVeinMineSwitchState = new HashMap<>();
    
    // 获取配置的统一方法
    public static ConfigItems getConfig() {
        return Config.getInstance().getConfigItems();
    }
    

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
        if (tool == null) return;

        // 检查工具是否能被损耗
        if (tool.isDamageable()) {
            //更好的耐久扣除方法 应该可以避免nep 因为担心有的mod可以左手使用工具 所以额外加了手的判断
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
            System.out.println("Protected Tool: "+tool.getItem());
            if (tool.getItem() != null) {
                return tool.getMaxDamage() - tool.getDamage() - getConfig().durabilityThreshold >= cost;
            }
        }
        // 如果不是指定的工具，或者工具不是工具类型，就不进行保护
        return true;
    }
    // 检查玩家工具是否需要保护
    public static boolean isToolProtected(ItemStack tool) {
        String toolName = tool.getItem().toString(); // 获取工具的名称（例如 "minecraft:diamond_pickaxe"）
        if(getConfig().protectAllDefaultValuableTools) {
            return getConfig().defaultProtectedTools.contains(toolName) || getConfig().protectedTools.contains(toolName);
        }
        return getConfig().protectedTools.contains(toolName); // 如果名称在保护列表中，返回 true
    }
}


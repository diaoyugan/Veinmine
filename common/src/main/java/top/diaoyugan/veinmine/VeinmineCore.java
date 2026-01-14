package top.diaoyugan.veinmine;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.diaoyugan.veinmine.config.ConfigItems;
import top.diaoyugan.veinmine.utils.Messages;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

import static top.diaoyugan.veinmine.utils.Utils.*;

public class VeinmineCore {

    public static void onBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity entity) {
        if (!getVeinMineSwitchState(player)) return; // 玩家未开启连锁采集，直接返回

        List<BlockPos> blocks = SmartVein.findBlocks(world, pos, BuiltInRegistries.BLOCK.getKey(state.getBlock()));
        if (blocks == null || blocks.isEmpty()) return; // 没有找到连锁方块

        if (!checkDurabilityAndWarn(player, state, blocks)) return; // 工具耐久不足，提示玩家并返回

        int destroyed = breakBlocks(world, player, pos, state, blocks); // 处理连锁破坏
        Utils.applyToolDurabilityDamage(player, destroyed); // 扣除耐久
    }

    private static boolean checkDurabilityAndWarn(Player player, BlockState state, List<BlockPos> blocks) {
        ConfigItems config = Utils.getConfig();
        if (!config.protectTools || player.hasInfiniteMaterials()) return true;

        int totalCost = Utils.calculateTotalDurabilityCost(blocks, player, state);
        if (Utils.hasEnoughDurability(player, totalCost)) return true;

        if (player instanceof ServerPlayer serverPlayer) {
            // 提示玩家耐久不足
            Component msg = Component.translatable("vm.warn.breakthroughs").withStyle(s -> s.applyFormat(ChatFormatting.RED));
            Messages.sendMessage(serverPlayer, msg, true);
        }
        return false;
    }

    private static int breakBlocks(Level world, Player player, BlockPos centerPos, BlockState originalState, List<BlockPos> blocks) {
        int count = 0;
        for (BlockPos pos : blocks) {
            if (!pos.equals(centerPos)) {
                count += breakSingleBlock(world, player, centerPos, pos, originalState);
            }
        }
        return count;
    }

    private static int breakSingleBlock(Level world, Player player, BlockPos centerPos, BlockPos targetPos, BlockState originalState) {
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.getBlock() != originalState.getBlock()) return 0; // 不同方块跳过

        if (shouldDropItems(player, targetState, world, targetPos)) {
            // 掉落物品
            Block.dropResources(targetState, world, targetPos, world.getBlockEntity(targetPos), player, player.getMainHandItem());
        }

        world.destroyBlock(targetPos, false); // 破坏方块

        if (world instanceof ServerLevel serverWorld) moveDropsToCenter(serverWorld, centerPos); // 移动掉落到中心
        return 1;
    }

    private static boolean shouldDropItems(Player player, BlockState state, Level world, BlockPos pos) {
        if (player.hasInfiniteMaterials()) return false; // 创造模式不掉落
        if (!Utils.isToolSuitable(state, player)) return false; // 工具不适合不掉落
        return !Utils.shouldNotDropItem(state, world, pos, player); // 判断方块是否会掉落
    }

    private static void moveDropsToCenter(ServerLevel world, BlockPos centerPos) {
        Vec3 center = Vec3.atCenterOf(centerPos);
        // 查找中心附近的掉落物品和经验球
        List<Entity> drops = world.getEntitiesOfClass(
                Entity.class,
                new AABB(centerPos).inflate(6),
                e -> e instanceof ItemEntity || e instanceof ExperienceOrb
        );
        drops.forEach(drop -> drop.setPos(center)); // 移动到中心位置
    }
}
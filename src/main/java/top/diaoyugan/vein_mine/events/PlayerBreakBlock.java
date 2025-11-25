package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.config.ConfigItems;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.List;

import static top.diaoyugan.vein_mine.utils.Utils.*;

public class PlayerBreakBlock {

    public static void register() {
        // 注册方块破坏事件
        PlayerBlockBreakEvents.AFTER.register(PlayerBreakBlock::onBlockBreak);
    }

    private static void onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        if (!getVeinMineSwitchState(player)) return; // 玩家未开启连锁采集，直接返回

        List<BlockPos> blocks = SmartVein.findBlocks(world, pos, Registries.BLOCK.getId(state.getBlock()));
        if (blocks == null || blocks.isEmpty()) return; // 没有找到连锁方块

        if (!checkDurabilityAndWarn(player, state, blocks)) return; // 工具耐久不足，提示玩家并返回

        int destroyed = breakBlocks(world, player, pos, state, blocks); // 处理连锁破坏
        Utils.applyToolDurabilityDamage(player, destroyed); // 扣除耐久
    }

    private static boolean checkDurabilityAndWarn(PlayerEntity player, BlockState state, List<BlockPos> blocks) {
        ConfigItems config = Utils.getConfig();
        if (!config.protectTools || player.isInCreativeMode()) return true;

        int totalCost = Utils.calculateTotalDurabilityCost(blocks, player, state);
        if (Utils.hasEnoughDurability(player, totalCost)) return true;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            // 提示玩家耐久不足
            Text msg = Text.translatable("vm.warn.breakthroughs").styled(s -> s.withFormatting(Formatting.RED));
            Messages.sendMessage(serverPlayer, msg, true);
        }
        return false;
    }

    private static int breakBlocks(World world, PlayerEntity player, BlockPos centerPos, BlockState originalState, List<BlockPos> blocks) {
        int count = 0;
        for (BlockPos pos : blocks) {
            if (!pos.equals(centerPos)) {
                count += breakSingleBlock(world, player, centerPos, pos, originalState);
            }
        }
        return count;
    }

    private static int breakSingleBlock(World world, PlayerEntity player, BlockPos centerPos, BlockPos targetPos, BlockState originalState) {
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.getBlock() != originalState.getBlock()) return 0; // 不同方块跳过

        if (shouldDropItems(player, targetState, world, targetPos)) {
            // 掉落物品
            Block.dropStacks(targetState, world, targetPos, world.getBlockEntity(targetPos), player, player.getMainHandStack());
        }

        world.breakBlock(targetPos, false); // 破坏方块

        if (world instanceof ServerWorld serverWorld) moveDropsToCenter(serverWorld, centerPos); // 移动掉落到中心
        return 1;
    }

    private static boolean shouldDropItems(PlayerEntity player, BlockState state, World world, BlockPos pos) {
        if (player.isInCreativeMode()) return false; // 创造模式不掉落
        if (!Utils.isToolSuitable(state, player)) return false; // 工具不适合不掉落
        return !Utils.shouldNotDropItem(state, world, pos, player); // 判断方块是否会掉落
    }

    private static void moveDropsToCenter(ServerWorld world, BlockPos centerPos) {
        Vec3d center = Vec3d.ofCenter(centerPos);
        // 查找中心附近的掉落物品和经验球
        List<Entity> drops = world.getEntitiesByClass(
                Entity.class,
                new Box(centerPos).expand(6),
                e -> e instanceof ItemEntity || e instanceof ExperienceOrbEntity
        );
        drops.forEach(drop -> drop.setPosition(center)); // 移动到中心位置
    }
}
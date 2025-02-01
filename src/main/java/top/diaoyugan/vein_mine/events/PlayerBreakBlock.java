package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.diaoyugan.vein_mine.utils.Utils.*;

public class PlayerBreakBlock {

    public static void register() {
        // 注册事件
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, entity) -> veinmine(world, player, pos, state)));
    }

    private static void veinmine(World world, PlayerEntity player, BlockPos pos, BlockState state) {
        if (!getVeinMineSwitchState(player)) return; // 确保玩家开启了连锁采集

        int destroyedCount = 0;
        Identifier startBlockID = Registries.BLOCK.getId(state.getBlock());
        List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos, startBlockID);
        List<ItemStack> drops = new ArrayList<>();

        for (BlockPos targetPos : blocksToBreak) {
            if (targetPos.equals(pos)) continue; // 排除中心方块

            BlockState targetState = world.getBlockState(targetPos);
            Block targetBlock = targetState.getBlock();
            if (targetBlock != state.getBlock()) continue;

            // 计算掉落物和连锁目标
            if (player.isInCreativeMode()) {
                world.breakBlock(targetPos, false);
            } else if (!Utils.isToolSuitable(targetState, player)) {
                world.breakBlock(targetPos, false);
                destroyedCount++;
            } else if (Utils.shouldNotDropItem(targetState, world, targetPos)) {
                world.breakBlock(targetPos, false);
                destroyedCount++;
            } else if (isContainer(targetState)) {
                world.breakBlock(targetPos, true);
                destroyedCount++;
            } else if (isSilktouch(player)) {
                world.breakBlock(targetPos, false);
                drops.add(new ItemStack(targetBlock)); // 精准采集掉落方块本身
                destroyedCount++;
            } else {
                // 获取普通掉落物
                ServerWorld serverWorld = (ServerWorld) world;
                List<ItemStack> blockDrops = Block.getDroppedStacks(targetState, serverWorld, targetPos, world.getBlockEntity(targetPos), player, player.getMainHandStack());
                drops.addAll(blockDrops);
                world.breakBlock(targetPos, false);
                destroyedCount++;
            }
        }

        // 合并掉落物
        Map<Item, Integer> dropCounts = new HashMap<>();
        for (ItemStack stack : drops) {
            dropCounts.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }

        // 在中心点生成合并后的掉落物
        for (Map.Entry<Item, Integer> entry : dropCounts.entrySet()) {
            ItemStack combinedStack = new ItemStack(entry.getKey(), entry.getValue());
            Block.dropStack(world, pos, combinedStack);
        }

        // 扣除耐久
        Utils.applyToolDurabilityDamage(player, destroyedCount);
    }



}

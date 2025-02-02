package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.List;

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
        if (blocksToBreak != null){
            for (BlockPos targetPos : blocksToBreak) {
                if (targetPos.equals(pos)) continue; // 排除中心方块
                //这的顺序不能改！！！
                BlockState targetState = world.getBlockState(targetPos);
                Block targetBlock = targetState.getBlock();
                if (targetBlock != state.getBlock()) continue;
                if (player.isInCreativeMode()) {
                    world.breakBlock(targetPos, false);
                } else if(!Utils.isToolSuitable(targetState, player)){
                    world.breakBlock(targetPos, false);
                    destroyedCount++;
                } else if(Utils.shouldNotDropItem(targetState, world, targetPos)){
                    world.breakBlock(targetPos, false);
                    destroyedCount++;
                }else if (isContainer(targetState)) {
                    world.breakBlock(targetPos, true);
                    destroyedCount++;
                } else if (isSilktouch(player)) {
                    world.breakBlock(targetPos, false);
                    Block.dropStack(world, pos, new ItemStack(targetBlock));
                    destroyedCount++;
                } else {
                    world.breakBlock(targetPos, true);
                    destroyedCount++;
                }
            // **移动掉落物到中心点**
                // **移动掉落物到中心点**
                if (world instanceof ServerWorld serverWorld) {
                    List<Entity> drops = serverWorld.getEntitiesByClass(Entity.class, new Box(pos).expand(6), e ->
                            e instanceof ItemEntity || e instanceof ExperienceOrbEntity
                    );
                    for (Entity drop : drops) {
                        drop.setPosition(Vec3d.ofCenter(pos)); // 传送到中心点
                    }
                }
            }


        // 扣除耐久
        Utils.applyToolDurabilityDamage(player, destroyedCount);
    }
    }



}

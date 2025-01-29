package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.ItemStack;

import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.diaoyugan.vein_mine.keybindreciever.NetworkingKeybindPacket;
import top.diaoyugan.vein_mine.utils.Utils;


public class PlayerBreakBlock {
    public static final Logger LOGGER = LoggerFactory.getLogger("InteractionEvents");

    public static void register() {
        // 注册事件
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, entity) -> veinmine(world, player, pos, state)));
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

    private static boolean isSilktouch(PlayerEntity player) {
        ItemStack tool = player.getMainHandStack();
        // 检查工具是否具有精准采集的附魔
        return tool.getEnchantments().toString().contains("minecraft:silk_touch");
    }

    private static void veinmine(World world, PlayerEntity player, BlockPos pos, BlockState state){
        LOGGER.info("Block {} broken at {}, {}, {} (client-side = {})",state.getBlock() ,pos.getX(), pos.getY(), pos.getZ(), world.isClient());

        LOGGER.info("玩家破坏了方块: {}", state.getBlock());

        int radius = 1;  // 设置搜索半径，1表示上下左右斜对角的8个方块，再加上中心方块
        int destroyedCount = 0;
        if (NetworkingKeybindPacket.getSwitchState()) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos targetPos = pos.add(x, y, z);
                        if (!targetPos.equals(pos)) {  // 排除中心方块
                            BlockState targetState = world.getBlockState(targetPos);
                            if (targetState.getBlock() == state.getBlock()) {
                                ItemStack dropStack = new ItemStack(targetState.getBlock());
                                if (player.isInCreativeMode()|| !Utils.isToolSuitable(targetState, player)) {
                                    world.breakBlock(targetPos, false);
                                    destroyedCount++;
                                } else if(isSilktouch(player)){
                                    world.breakBlock(targetPos, false);
                                    Block.dropStack(world, targetPos, dropStack);
                                    destroyedCount++;
                                }else
                                {
                                    // 破坏该方块
                                    world.breakBlock(targetPos, true);  // true 表示破坏时掉落物品
                                    destroyedCount++;
                                }
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("共破坏了 {} 个相邻的相同类型方块。", destroyedCount);
        if (!player.isInCreativeMode()) {
            applyToolDurabilityDamage(player, destroyedCount);
        }
    }
}

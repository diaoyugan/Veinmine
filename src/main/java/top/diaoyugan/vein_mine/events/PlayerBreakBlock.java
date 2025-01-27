package top.diaoyugan.vein_mine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerBreakBlock {
    public static final Logger LOGGER = LoggerFactory.getLogger("InteractionEvents");

    public static void register() {
        // 注册事件
        PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, entity) -> state.getBlock() != Blocks.BEDROCK));

        PlayerBlockBreakEvents.CANCELED.register(((world, player, pos, state, entity) -> LOGGER.info("Block break event canceled at {}, {}, {} (client-side = {})", pos.getX(), pos.getY(), pos.getZ(), world.isClient())));

        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, entity) -> vinemine(world, player, pos, state)));
    }

    private static void vinemine(World world, PlayerEntity player, BlockPos pos, BlockState state){
        LOGGER.info("Block {} broken at {}, {}, {} (client-side = {})",state.getBlock() ,pos.getX(), pos.getY(), pos.getZ(), world.isClient());

        LOGGER.info("玩家破坏了方块: {}", state.getBlock());

        int radius = 1;  // 设置搜索半径，1表示上下左右斜对角的8个方块，再加上中心方块
        int destroyedCount = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.add(x, y, z);
                    if (!targetPos.equals(pos)) {  // 排除中心方块
                        BlockState targetState = world.getBlockState(targetPos);
                        if (targetState.getBlock() == state.getBlock()) {
                            // 破坏该方块
                            world.breakBlock(targetPos, true);  // true 表示破坏时掉落物品
                            destroyedCount++;
                        }
                    }
                }
            }
        }

        // 打印破坏的方块数量
        LOGGER.info("共破坏了 {} 个相邻的相同类型方块。", destroyedCount);
    }
}

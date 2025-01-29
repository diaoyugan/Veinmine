package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.LoggerFactory;

public class Logger {
    public static final String ID = "veinmine";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ID);

    // throwLog 方法处理不同类型的日志
    public static void throwLog(String type, String message, Object... params) {
        String formattedMessage = String.format(message, params);
        switch (type) {
            case "warn" -> LOGGER.warn(formattedMessage);
            case "error" -> LOGGER.error(formattedMessage);
            case "debug" -> LOGGER.debug(formattedMessage);
            default -> LOGGER.info(formattedMessage);
        }
    }


    public static void newLogBlockBroken(BlockState state, BlockPos pos, World world, int destroyedCount) {
        // 通过 throwLog 来记录日志
        throwLog("info", "Block {} broken at {}, {}, {} (client-side = {})", state.getBlock(), pos.getX(), pos.getY(), pos.getZ(), world.isClient());
        throwLog("info", "玩家破坏了方块: {}", state.getBlock());
        throwLog("info", "共破坏了 {} 个相邻的相同类型方块。", destroyedCount);
    }
}

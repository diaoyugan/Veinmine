package top.diaoyugan.vein_mine.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import top.diaoyugan.vein_mine.utils.logging.Logger;
import top.diaoyugan.vein_mine.utils.logging.LoggerLevels;


public class MessagesOverride {
    public static void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        try {
            if (player == null) throw new NullPointerException("player is null");
            if (message == null) throw new NullPointerException("message is null");
            if (isOnActionbar == null) throw new NullPointerException("isOnActionbar is null");

            var server = player.getWorld().getServer();
            if (server == null) throw new NullPointerException("player.getWorld().getServer() is null");
            server.execute(() -> player.sendMessage(message, isOnActionbar));
        } catch (NullPointerException e) {
            // 输出错误和堆栈，但不抛出异常
            Logger.throwLog(LoggerLevels.ERROR,"Failed to send message due to null parameter!",e);
        }
    }
}

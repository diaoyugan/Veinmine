package top.diaoyugan.vein_mine.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.vein_mine.utils.logging.Logger;
import top.diaoyugan.vein_mine.utils.logging.LoggerLevels;

public class MessagesOverride {

    public static void sendMessage(ServerPlayer player, Component message, Boolean isOnActionbar) {
        try {
            if (player == null) throw new NullPointerException("player is null");
            if (message == null) throw new NullPointerException("message is null");
            if (isOnActionbar == null) throw new NullPointerException("isOnActionbar is null");

            var server = player.level().getServer();

            server.execute(() -> player.displayClientMessage(message, isOnActionbar));
        } catch (NullPointerException e) {
            // 输出错误和堆栈，但不抛出异常
            Logger.throwLog(LoggerLevels.ERROR,"Failed to send message due to null parameter!",e);
        }
    }
}
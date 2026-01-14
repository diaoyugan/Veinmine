package top.diaoyugan.veinmine.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.utils.logging.Logger;
import top.diaoyugan.veinmine.utils.logging.LoggerLevels;


/**
 * 消息发送工具类，用于向玩家发送各种消息。
 */
public class Messages {

    /**
     * 仅发送客户端消息，不经过服务端。
     *
     * @param message       消息内容
     * @param isOnActionbar 是否显示在动作条上，否则显示在聊天栏
     */
    public static void clientMessage(Component message, Boolean isOnActionbar) {
        Minecraft client = Minecraft.getInstance();
        if (isOnActionbar)
            client.gui.setOverlayMessage(message, false);
        else
            client.gui.getChat().addMessage(message);
    }

    /**
     * 发送聊天消息给服务端玩家。
     *
     * @param player        目标玩家
     * @param message       消息内容
     * @param isOnActionbar 是否显示在动作条上，否则显示在聊天栏
     */
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

    /**
     * 发送标题消息给服务端玩家（屏幕中间的大标题）。
     *
     * @param player 目标玩家
     * @param title  标题内容
     */
    public static void sendTitleMessage(ServerPlayer player, Component title) {
        player.connection.send(new ClientboundSetTitleTextPacket(title));
    }

    /**
     * 发送带副标题的标题消息（副标题在标题显示之后显示）。
     *
     * @param player   目标玩家
     * @param title    大标题内容
     * @param subtitle 副标题内容
     */
    public static void sendTitleMessage(ServerPlayer player, Component title, Component subtitle) {
        player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
        player.connection.send(new ClientboundSetTitleTextPacket(title));
    }
}
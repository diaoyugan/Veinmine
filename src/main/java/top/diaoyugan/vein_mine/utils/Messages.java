package top.diaoyugan.vein_mine.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


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
    @Environment(EnvType.CLIENT)
    public static void clientMessage(Text message, Boolean isOnActionbar) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (isOnActionbar)
            client.inGameHud.setOverlayMessage(message, false);
        else
            client.inGameHud.getChatHud().addMessage(message);
    }

    /**
     * 发送聊天消息给服务端玩家。
     *
     * @param player        目标玩家
     * @param message       消息内容
     * @param isOnActionbar 是否显示在动作条上，否则显示在聊天栏
     */
    public static void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        IUtilsVersion vMessages = new VersionMessage();
        vMessages.sendMessage(player, message, isOnActionbar);
    }

    /**
     * 发送标题消息给服务端玩家（屏幕中间的大标题）。
     *
     * @param player 目标玩家
     * @param title  标题内容
     */
    public static void sendTitleMessage(ServerPlayerEntity player, Text title) {
        player.networkHandler.sendPacket(new TitleS2CPacket(title));
    }

    /**
     * 发送带副标题的标题消息（副标题在标题显示之后显示）。
     *
     * @param player   目标玩家
     * @param title    大标题内容
     * @param subtitle 副标题内容
     */
    public static void sendTitleMessage(ServerPlayerEntity player, Text title, Text subtitle) {
        player.networkHandler.sendPacket(new SubtitleS2CPacket(subtitle));
        player.networkHandler.sendPacket(new TitleS2CPacket(title));
    }
}
package top.diaoyugan.vein_mine.utils;

import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class Messages {

    //发送聊天消息
    public static void sendMessage(ServerPlayerEntity player, Text message,Boolean isOnActionbar) {
        if (isOnActionbar)
            player.server.execute(() -> player.sendMessage(message, true));
        else
            player.server.execute(() -> player.sendMessage(message, false));
    }

    //发送标题消息(游戏中屏幕中间的大标题）
    public static void sendTitleMessage(ServerPlayerEntity player, Text title) {
        player.networkHandler.sendPacket(new TitleS2CPacket(title));
    }

    //附带副标题(大标题下面的小字）
    public static void sendTitleMessage(ServerPlayerEntity player, Text title, Text subtitle) {
        player.networkHandler.sendPacket(new SubtitleS2CPacket(subtitle));
        player.networkHandler.sendPacket(new TitleS2CPacket(title));
    }
}

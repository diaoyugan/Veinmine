package top.diaoyugan.vein_mine.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class Messages {
    @Environment(EnvType.CLIENT)
    //客户端消息 不会经过服务端
    public static void clientMessage(Text message,Boolean isOnActionbar){
        MinecraftClient client = MinecraftClient.getInstance();
        if(isOnActionbar)
            client.inGameHud.setOverlayMessage(message, false);
        else
            client.inGameHud.getChatHud().addMessage(message);
    }

    //发送聊天消息
    public static void sendMessage(ServerPlayerEntity player, Text message,Boolean isOnActionbar) {
        IUtilsVersion vMessages = new VersionMessage();
        vMessages.sendMessage(player, message, isOnActionbar);
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

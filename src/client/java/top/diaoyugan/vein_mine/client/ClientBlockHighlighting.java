package top.diaoyugan.vein_mine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.util.Identifier;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;
import top.diaoyugan.vein_mine.utils.Logger;



public class ClientBlockHighlighting {

    //在完成高亮共享前 不要注册这个
    public static final Identifier ID = HighlightBlock.HIGHLIGHT_PACKET_ID;

    // 在客户端初始化时注册监听器
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(HighlightBlock.BlockHighlightPayload.ID, (payload, context) -> {
            Logger.throwLog("info", "Received highlight pos" + payload.blockPos());
        });
    }
}
package top.diaoyugan.vein_mine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindingPayload;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindingPayloadResponse;

public class HotKeys {
    private static boolean veinMineSwitchState = false;  // 用来保存从服务器接收到的状态

    // 接收服务端返回的开关状态
    protected static void receiveKeybindingResponse(KeybindingPayloadResponse response, ClientPlayNetworking.Context context) {
        veinMineSwitchState = response.state();  // 更新客户端的开关状态
    }

    public static void tickEvent(MinecraftClient client){
        if (client.getNetworkHandler() != null) {
            if (client.player != null && veinMineSwitchState) {
                ClientBlockHighlighting.checkPlayerLooking(client.player);
            }

            // 按下切换按键时，发送请求包
            if (vein_mineClient.BINDING.wasPressed()) {
                ClientPlayNetworking.send(KeybindingPayload.INSTANCE);
            }
        }

        if (client.player != null && !veinMineSwitchState) {
            ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.clear();
        }
    }
}

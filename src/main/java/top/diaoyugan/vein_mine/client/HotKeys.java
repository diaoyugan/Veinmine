package top.diaoyugan.vein_mine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindingPayload;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindingPayloadResponse;
import top.diaoyugan.vein_mine.utils.Utils;

public class HotKeys {
    private static boolean veinMineSwitchState = false; // 当前状态
    private static boolean lastPressed = false;         // 用于按住模式的状态比较

    // 接收服务端返回的状态
    protected static void receiveKeybindingResponse(KeybindingPayloadResponse response, ClientPlayNetworking.Context context) {
        veinMineSwitchState = response.state();  // 同步状态
    }

    public static void tickEvent(MinecraftClient client) {
        if (client.getNetworkHandler() == null || client.player == null) return;

        boolean isPressed = vein_mineClient.BINDING.isPressed();

        if (Utils.getConfig().useHoldInsteadOfToggle) {
            // 【按住模式】只在状态变化时发送包
            if (isPressed != lastPressed) {
                lastPressed = isPressed;
                ClientPlayNetworking.send(KeybindingPayload.INSTANCE);
            }
        } else {
            // 【切换模式】按一下切换一次状态
            if (vein_mineClient.BINDING.wasPressed()) {
                ClientPlayNetworking.send(KeybindingPayload.INSTANCE);
            }
        }

        // 功能执行与否根据服务端返回状态决定
        if (veinMineSwitchState) {
            ClientBlockHighlighting.checkPlayerLooking(client.player);
        } else {
            ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.clear();
        }
    }
}


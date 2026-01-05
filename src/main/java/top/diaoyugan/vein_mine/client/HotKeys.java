package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import top.diaoyugan.vein_mine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.vein_mine.networking.keypacket.KeyResponsePacket;
import top.diaoyugan.vein_mine.utils.Utils;

public class HotKeys {
    private static boolean veinMineSwitchState = false; // 当前状态
    private static boolean lastPressed = false;         // 用于按住模式的状态比较

    // 接收服务端返回的状态
    protected static void receiveKeybindingResponse(KeyResponsePacket response, ClientPlayNetworking.Context context) {
        veinMineSwitchState = response.state();  // 同步状态
    }
    // 获取客户端状态
    @Environment(EnvType.CLIENT)
    public static boolean getVeinMineSwitchState(){
        return veinMineSwitchState;
    }

    public static void tickEvent(MinecraftClient client) {
        ClientVersionInterface CLI = new CLInterfaceOverride();
        if (client.getNetworkHandler() == null || client.player == null) return;

        boolean isPressed = CLI.KeyIsPressed();

        if (Utils.getConfig().useHoldInsteadOfToggle) {
            // 【按住模式】只在状态变化时发送包
            if (isPressed != lastPressed) {
                lastPressed = isPressed;
                ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
            }
        } else {
            // 【切换模式】按一下切换一次状态
            if (CLI.KeyWasPressed()) {
                ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
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


package top.diaoyugan.veinmine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.client.hotkey.HotKeyLogic;
import top.diaoyugan.veinmine.client.hotkey.HotKeyState;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;
import top.diaoyugan.veinmine.utils.Utils;

public final class HotKeys {
    private static boolean lastPressed = false;
    // 服务端 → 客户端
    static void receiveKeybindingResponse(
            KeyResponsePacket response,
            ClientPlayNetworking.Context context
    ) {
        HotKeyState.updateFromServer(response.state());
    }

    public static boolean getVeinMineSwitchState() {
        return HotKeyState.isVeinMineEnabled();
    }

    public static void tickEvent(Minecraft client) {
        if (client.player == null || client.getConnection() == null) return;

        boolean isPressed = KeyBinding.BINDING.isDown();
        boolean click = KeyBinding.BINDING.consumeClick();
        boolean useHold = Utils.getConfig().useHoldInsteadOfToggle;

        // 按键处理 → 根据模式决定是否发送 KeyPress 包
        if (useHold) {
            if (HotKeyState.consumeLastPressedChange(isPressed) && isPressed) {
                ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
            }
        } else {
            if (click) {
                ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
            }
        }

        // 客户端高亮逻辑，根据服务端状态决定
        if (HotKeyState.isVeinMineEnabled()) {
            BlockPos pos = ClientHighlightLogic.getLookedBlock(client.player);
            if(pos != null) {
                ClientPlayNetworking.send(new BlockHighlightRequest(pos));
            }
        } else {
            ClientHighlightState.HIGHLIGHTED_BLOCKS.clear();
        }
    }

}

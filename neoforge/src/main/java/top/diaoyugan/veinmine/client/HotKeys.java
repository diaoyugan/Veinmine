package top.diaoyugan.veinmine.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.client.hotkey.HotKeyState;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.utils.Utils;

public final class HotKeys {

    public static void tickEvent(Minecraft client) {
        if (client.player == null || client.getConnection() == null) return;

        boolean isPressed = KeyBinding.BINDING.isDown();
        boolean click = KeyBinding.BINDING.consumeClick();

        // 按键处理 → 根据模式决定是否发送 KeyPress 包
        if (Utils.getConfig().useHoldInsteadOfToggle) {
            if (HotKeyState.consumeLastPressedChange(isPressed) && isPressed) {
                ClientPacketDistributor.sendToServer(KeyPressPacket.INSTANCE);
            }
        } else {
            if (click) {
                ClientPacketDistributor.sendToServer(KeyPressPacket.INSTANCE);
            }
        }

        // 每帧上传准星方块位置（连锁计算请求）
        if (HotKeyState.isVeinMineEnabled()) {
            BlockPos pos = ClientHighlightLogic.getLookedBlock(client.player);
            if(pos != null) {
                ClientPacketDistributor.sendToServer(new BlockHighlightRequest(pos));
            }
        }
    }

}


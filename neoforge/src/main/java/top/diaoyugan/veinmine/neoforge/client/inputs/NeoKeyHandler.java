package top.diaoyugan.veinmine.neoforge.client.inputs;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.client.hotkey.ActivationKeyState;
import top.diaoyugan.veinmine.client.inputs.KeyBinding;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.utils.Utils;

public final class NeoKeyHandler {

    public static void tickEvent(Minecraft client) {
        if (client.player == null || client.getConnection() == null) return;

        boolean isPressed = KeyBinding.ACTIVATION_KEY.isDown();
        boolean click = KeyBinding.ACTIVATION_KEY.consumeClick();

        if (Utils.getConfig().useHoldInsteadOfToggle) {
            if (ActivationKeyState.consumeLastPressedChange(isPressed)) {
                ClientPacketDistributor.sendToServer(KeyPressPacket.INSTANCE);
            }
        } else {
            if (click) {
                ClientPacketDistributor.sendToServer(KeyPressPacket.INSTANCE);
            }
        }

        if (ActivationKeyState.isVeinMineEnabled()) {
            BlockPos pos = ClientHighlightLogic.getLookedBlock(client.player);
            if(pos != null) {
                ClientPacketDistributor.sendToServer(new BlockHighlightRequest(pos));
                ClientHighlightState.SHOW_HIGHLIGHT = true;
            }else {
                ClientHighlightState.SHOW_HIGHLIGHT = false;
            }
        }
        else {
            ClientHighlightState.HIGHLIGHTED_BLOCKS.clear();
        }
    }

}


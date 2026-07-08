package top.diaoyugan.veinmine.fabric.client.inputs;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.client.hotkey.ActivationKeyState;
import top.diaoyugan.veinmine.client.inputs.KeyBinding;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;
import top.diaoyugan.veinmine.utils.Utils;

public final class FabricKeyHandler {
    public static void receiveKeybindingResponse(
            KeyResponsePacket response,
            ClientPlayNetworking.Context context
    ) {
        ActivationKeyState.updateFromServer(response.state());
    }

    public static void tickEvent(Minecraft client) {
        if (client.player == null || client.getConnection() == null) return;

        boolean isPressed = KeyBinding.ACTIVATION_KEY.isDown();
        boolean click = KeyBinding.ACTIVATION_KEY.consumeClick();
        boolean useHold = Utils.getConfig().useHoldInsteadOfToggle;

        if (useHold) {
            if (ActivationKeyState.consumeLastPressedChange(isPressed)) {
                ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
            }
        } else if (click) {
            ClientPlayNetworking.send(KeyPressPacket.INSTANCE);
        }

        if (ActivationKeyState.isVeinMineEnabled()) {
            BlockPos pos = ClientHighlightLogic.getLookedBlock(client.player);
            if(pos != null) {
                ClientPlayNetworking.send(new BlockHighlightRequest(pos));
                ClientHighlightState.SHOW_HIGHLIGHT = true;
            }else {
                ClientHighlightState.SHOW_HIGHLIGHT = false;
            }
        } else {
            ClientHighlightState.HIGHLIGHTED_BLOCKS.clear();
        }
    }

}

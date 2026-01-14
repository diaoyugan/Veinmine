package top.diaoyugan.veinmine.client.hotkey;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.utils.Utils;

public final class HotKeyLogic {

    private HotKeyLogic() {}

    public static void tick(
            boolean keyDown,
            boolean keyClicked,
            LocalPlayer player,
            Runnable sendKeyPacket,
            java.util.function.Consumer<BlockPos> sendHighlightRequest
    ) {
        if (player == null) return;

        if (Utils.getConfig().useHoldInsteadOfToggle) {
            if (HotKeyState.consumeLastPressedChange(keyDown)) {
                sendKeyPacket.run();
            }
        } else {
            if (keyClicked) {
                sendKeyPacket.run();
            }
        }

        if (HotKeyState.isVeinMineEnabled()) {
            BlockPos pos = ClientHighlightLogic.getLookedBlock(player);
            if (pos != null) {
                sendHighlightRequest.accept(pos);
            } else {
                ClientHighlightState.clear();
            }
        } else {
            ClientHighlightState.clear();
        }
    }
}


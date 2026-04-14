package top.diaoyugan.vein_mine.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.networking.ClientNetBridge;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClientBlockHighlighting {
    public static final Set<BlockPos> HIGHLIGHTED_BLOCKS = new HashSet<>();

    public static void checkPlayerLooking(ClientPlayerEntity player) {
        HitResult hitResult = player.raycast(10, 0, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            ClientNetBridge.INSTANCE.sendHighlightRequest(blockPos);
        } else if (!HIGHLIGHTED_BLOCKS.isEmpty()) {
            HIGHLIGHTED_BLOCKS.clear();
        }
    }

    public static void applyHighlightedBlocks(List<BlockPos> positions) {
        Set<BlockPos> newBlocks = new HashSet<>(positions);
        if (!HIGHLIGHTED_BLOCKS.equals(newBlocks)) {
            HIGHLIGHTED_BLOCKS.clear();
            HIGHLIGHTED_BLOCKS.addAll(newBlocks);

            if (!HIGHLIGHTED_BLOCKS.isEmpty() && Utils.getConfig().highlightBlocksMessage) {
                Messages.clientMessage(
                        Text.translatable("vm.message.highlightblocks",
                                HIGHLIGHTED_BLOCKS.size()),
                        true);
            }
        }
    }
}

package top.diaoyugan.veinmine.client.highlight;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import top.diaoyugan.veinmine.utils.Messages;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.HashSet;
import java.util.Set;

public final class ClientHighlightLogic {

    private ClientHighlightLogic() {}

    public static BlockPos getLookedBlock(LocalPlayer player) {
        HitResult hit = player.pick(5, 0, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hit).getBlockPos();
        } else {
            return null;
        }
    }

    /** loader 在收到服务端高亮数据后调用 */
    public static void onHighlightResponse(Iterable<BlockPos> positions) {
        Set<BlockPos> newSet = new HashSet<>();
        for (BlockPos pos : positions) {
            newSet.add(pos);
        }

        boolean changed = ClientHighlightState.replace(newSet);

        if (changed
                && !ClientHighlightState.isEmpty()
                && Utils.getConfig().highlightBlocksMessage) {

            Messages.clientMessage(
                    Component.translatable(
                            "vm.message.highlightblocks",
                            ClientHighlightState.size()
                    ),
                    true
            );
        }
    }
}


package top.diaoyugan.veinmine.client.highlight;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.utils.Messages;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.HashSet;
import java.util.Set;

public final class ClientHighlightCallbacks {

    private ClientHighlightCallbacks() {}

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


    /** loader 在玩家没指到方块时调用 */
    public static void onMissedTarget() {
        if (!ClientHighlightState.isEmpty()) {
            ClientHighlightState.clear();
        }
    }
}


package top.diaoyugan.veinmine.client.highlight;

import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ClientHighlightState {

    public static final Set<BlockPos> HIGHLIGHTED_BLOCKS = new HashSet<>();
    public static boolean SHOW_HIGHLIGHT = true;

    private ClientHighlightState() {}

    public static boolean replace(Set<BlockPos> newBlocks) {
        if (HIGHLIGHTED_BLOCKS.equals(newBlocks)) return false;

        HIGHLIGHTED_BLOCKS.clear();
        HIGHLIGHTED_BLOCKS.addAll(newBlocks);
        return true;
    }

    public static boolean isEmpty() {
        return HIGHLIGHTED_BLOCKS.isEmpty();
    }

    public static int size() {
        return HIGHLIGHTED_BLOCKS.size();
    }
}

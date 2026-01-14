package top.diaoyugan.veinmine.client.highlight;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class ClientHighlightLogic {

    private ClientHighlightLogic() {}

    public static BlockPos getLookedBlock(LocalPlayer player) {
        HitResult hit = player.pick(player.getPickRadius(), 0, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hit).getBlockPos();
        } else {
            ClientHighlightState.HIGHLIGHTED_BLOCKS.clear();
            return null;
        }
    }
}


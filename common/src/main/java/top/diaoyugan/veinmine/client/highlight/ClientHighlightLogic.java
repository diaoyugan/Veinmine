package top.diaoyugan.veinmine.client.highlight;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class ClientHighlightLogic {

    private ClientHighlightLogic() {}

    public static BlockPos getLookedBlock(LocalPlayer player) {
        //TODO:密切关注这群傻逼什么时候改回来
        HitResult hit = player.pick(/*player.getPickRadius() 幽默ojang让这个值永远返回0*/ 5, 0, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hit).getBlockPos();
        } else {
            ClientHighlightState.HIGHLIGHTED_BLOCKS.clear();
            return null;
        }
    }
}


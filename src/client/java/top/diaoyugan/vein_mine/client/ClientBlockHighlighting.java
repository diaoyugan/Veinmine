package top.diaoyugan.vein_mine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;


public class ClientBlockHighlighting {

    public static void checkPlayerLooking(ClientPlayerEntity player) {
        // 获取玩家视线方向
        HitResult hitResult = player.raycast(10, 0, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            sendHighlightPacket(blockPos);
        }
    }

    public static void sendHighlightPacket(BlockPos blockPos) {
        ClientPlayNetworking.send(new HighlightBlock.BlockHighlightPayload(blockPos));
    }

}
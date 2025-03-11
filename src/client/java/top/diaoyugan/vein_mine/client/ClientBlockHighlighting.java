package top.diaoyugan.vein_mine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;

import java.util.HashSet;
import java.util.Set;


public class ClientBlockHighlighting {
    public static final Set<BlockPos> HIGHLIGHTED_BLOCKS = new HashSet<>();

    public static void onInitialize(){
        PayloadTypeRegistry.playS2C().register(HighlightBlock.BlockHighlightPayload.ID, HighlightBlock.BlockHighlightPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(HighlightBlock.BlockHighlightPayload.ID, ClientBlockHighlighting::receiveCL);
    }

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
    private static void receiveCL(HighlightBlock.BlockHighlightPayload payload, ClientPlayNetworking.Context context) {
        System.out.println("[VeinMine] Received highlight block pos: " + payload.blockPos());
        ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.add(payload.blockPos());
    }

}
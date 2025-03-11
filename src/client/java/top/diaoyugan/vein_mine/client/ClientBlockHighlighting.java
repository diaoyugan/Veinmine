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

    public static void onInitialize() {
        // 注册接收来自服务器的消息，改为接收 BlockHighlightPayloadS2C 类型的数据
        PayloadTypeRegistry.playS2C().register(HighlightBlock.BlockHighlightPayloadS2C.ID, HighlightBlock.BlockHighlightPayloadS2C.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(HighlightBlock.BlockHighlightPayloadS2C.ID, ClientBlockHighlighting::receiveCL);
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
        ClientPlayNetworking.send(new HighlightBlock.BlockHighlightPayloadC2S(blockPos));
    }

    private static void receiveCL(HighlightBlock.BlockHighlightPayloadS2C payload, ClientPlayNetworking.Context context) {
        // 将接收到的多个 BlockPos 转换为 Set
        Set<BlockPos> newBlocks = new HashSet<>(payload.arrayList());

        // 如果新旧内容不同，就替换
        if (!ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.equals(newBlocks)) {
            ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.clear();
            ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.addAll(newBlocks);
        }

        System.out.println("[VeinMine] Received highlight block pos: " + newBlocks);
    }
}

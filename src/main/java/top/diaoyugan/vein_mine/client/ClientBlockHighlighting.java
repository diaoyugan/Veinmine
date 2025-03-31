package top.diaoyugan.vein_mine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;
import top.diaoyugan.vein_mine.utils.Messages;

import java.util.HashSet;
import java.util.Set;


public class ClientBlockHighlighting {
    public static final Set<BlockPos> HIGHLIGHTED_BLOCKS = new HashSet<>();

    public static void onInitialize() {
        // 注册客户端接收处理器
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

            // 只有在数据更新且不为空时输出
            if (!ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.isEmpty()) {
                Messages.clientMessage(
                        Text.translatable("vm.message.highlightblocks",
                                ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.size()),
                        true);
            }
        }
    }

}

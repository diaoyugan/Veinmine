package top.diaoyugan.vein_mine.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.*;

public class HighlightBlockOverride {
    protected static final Map<UUID, Set<BlockPos>> playerGlowingBlocks = new HashMap<>();

    public static void receive(HighlightBlock.BlockHighlightPayloadC2S payload, ServerPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        ServerPlayerEntity player = context.player();
        ServerWorld world = (ServerWorld) player.getWorld();

        // 获取方块的命名空间 ID
        Set<BlockPos> newGlowingBlocks = new HashSet<>();

        if (Utils.getVeinMineSwitchState(player)) {
            List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos);
            if (blocksToBreak != null) {


                // 收集所有需要发送的方块
                newGlowingBlocks.addAll(blocksToBreak);

                ServerPlayNetworking.send(player, new HighlightBlock.BlockHighlightPayloadS2C(new ArrayList<>(newGlowingBlocks)));

            }
        }

        // 更新存储的活跃实体
        Set<BlockPos> playerGlowingPos = playerGlowingBlocks.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        playerGlowingPos.clear();
        playerGlowingPos.addAll(newGlowingBlocks);
    }
}

package top.diaoyugan.vein_mine.Networking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;


import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;


import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.*;


public class HighlightBlock implements ModInitializer {
    public static final Identifier HIGHLIGHT_PACKET_ID = Networking.id("block_highlight");

    public record BlockHighlightPayload(BlockPos blockPos) implements CustomPayload {
        public static final Id<BlockHighlightPayload> ID = new CustomPayload.Id<>(HighlightBlock.HIGHLIGHT_PACKET_ID);
        public static final PacketCodec<PacketByteBuf, BlockHighlightPayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockHighlightPayload::blockPos, BlockHighlightPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(BlockHighlightPayload.ID, BlockHighlightPayload.CODEC);
        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, BlockHighlightPayload.ID, HighlightBlock::receive));
    }

    private static final Map<UUID, Set<BlockPos>> playerGlowingBlocks = new HashMap<>();

    private static void receive(BlockHighlightPayload payload, ServerPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        ServerPlayerEntity player = context.player();
        ServerWorld world = (ServerWorld) player.getWorld();

        // 获取方块的命名空间 ID
        Set<BlockPos> newGlowingBlocks = new HashSet<>();

        if (Utils.getVeinMineSwitchState(player)) {
                List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos);
                if (blocksToBreak != null) {
                    Set<BlockPos> oldSentBlocks = playerGlowingBlocks.getOrDefault(player.getUuid(), Set.of());
                    Set<BlockPos> newBlockSet = new HashSet<>(blocksToBreak);

                    boolean shouldSend = !newBlockSet.equals(oldSentBlocks);

                    for (BlockPos targetPos : blocksToBreak) {
                        newGlowingBlocks.add(targetPos);
                        //spawnGlowingBlock(world, targetPos, player);

                        // ✨ 如果坐标改变了才发给客户端
                        if (shouldSend) {
                            ServerPlayNetworking.send(player, new BlockHighlightPayload(targetPos));
                        }
                    }
                }
        }

        // 更新存储的活跃实体
        Set<BlockPos> playerGlowingPos = playerGlowingBlocks.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        playerGlowingPos.clear();
        playerGlowingPos.addAll(newGlowingBlocks);
    }


}

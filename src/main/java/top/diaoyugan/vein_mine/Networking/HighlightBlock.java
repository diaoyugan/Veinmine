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
    public static final Identifier HIGHLIGHT_PACKET_ID = Networking.id("block_highlight"); // 请求包的 ID
    public static final Identifier HIGHLIGHT_PACKET_RESPONSE_ID = Networking.id("block_highlight_response"); // 响应包的 ID

    public record BlockHighlightPayloadC2S(BlockPos blockPos) implements CustomPayload {
        public static final Id<BlockHighlightPayloadC2S> ID = new CustomPayload.Id<>(HighlightBlock.HIGHLIGHT_PACKET_ID); // 请求包的 ID
        public static final PacketCodec<PacketByteBuf, BlockHighlightPayloadC2S> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockHighlightPayloadC2S::blockPos, BlockHighlightPayloadC2S::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record BlockHighlightPayloadS2C(ArrayList<BlockPos> arrayList) implements CustomPayload {
        public static final Id<BlockHighlightPayloadS2C> ID = new CustomPayload.Id<>(HighlightBlock.HIGHLIGHT_PACKET_RESPONSE_ID); // 响应包的 ID

        // 创建一个自定义的 PacketCodec 用于编码和解码 ArrayList<BlockPos>
        public static final PacketCodec<PacketByteBuf, BlockHighlightPayloadS2C> CODEC = new PacketCodec<>() {
            @Override
            public BlockHighlightPayloadS2C decode(PacketByteBuf buf) {
                int size = buf.readInt();
                ArrayList<BlockPos> arrayList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    arrayList.add(BlockPos.fromLong(buf.readLong()));
                }
                return new BlockHighlightPayloadS2C(arrayList);
            }

            @Override
            public void encode(PacketByteBuf buf, BlockHighlightPayloadS2C value) {
                buf.writeInt(value.arrayList.size()); // 写入列表的大小
                for (BlockPos pos : value.arrayList) {
                    buf.writeLong(pos.asLong()); // 将每个 BlockPos 编码成 long 类型
                }
            }
        };

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }


    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(BlockHighlightPayloadC2S.ID, BlockHighlightPayloadC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(HighlightBlock.BlockHighlightPayloadS2C.ID, HighlightBlock.BlockHighlightPayloadS2C.CODEC);

        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, BlockHighlightPayloadC2S.ID, HighlightBlock::receive));
    }

    private static final Map<UUID, Set<BlockPos>> playerGlowingBlocks = new HashMap<>();

    private static void receive(BlockHighlightPayloadC2S payload, ServerPlayNetworking.Context context) {
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

                // 收集所有需要发送的方块
                newGlowingBlocks.addAll(blocksToBreak);

                // 如果方块集合有变动才发送
                if (shouldSend) {
                    // 这里一次性发送所有需要高亮的方块
                    ServerPlayNetworking.send(player, new BlockHighlightPayloadS2C(new ArrayList<>(newGlowingBlocks)));
                }
            }
        }

        // 更新存储的活跃实体
        Set<BlockPos> playerGlowingPos = playerGlowingBlocks.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        playerGlowingPos.clear();
        playerGlowingPos.addAll(newGlowingBlocks);
    }



}

package top.diaoyugan.vein_mine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;




import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import top.diaoyugan.vein_mine.SVInterfaceOverride;
import top.diaoyugan.vein_mine.utils.ServerVersionInterface;

import java.util.*;


public class HighlightBlock {
    static ServerVersionInterface SVI = new SVInterfaceOverride();
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


    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(BlockHighlightPayloadC2S.ID, BlockHighlightPayloadC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(HighlightBlock.BlockHighlightPayloadS2C.ID, HighlightBlock.BlockHighlightPayloadS2C.CODEC);

        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, BlockHighlightPayloadC2S.ID, HighlightBlock::receive));
    }



    private static void receive(BlockHighlightPayloadC2S payload, ServerPlayNetworking.Context context) {
        SVI.PacketReceive(payload, context);
    }
}

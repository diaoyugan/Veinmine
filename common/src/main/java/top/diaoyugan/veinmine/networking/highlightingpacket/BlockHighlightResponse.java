package top.diaoyugan.veinmine.networking.highlightingpacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.diaoyugan.veinmine.networking.Networking;

import java.util.ArrayList;


public record BlockHighlightResponse(ArrayList<BlockPos> positions) implements CustomPacketPayload {
    public static final Type<BlockHighlightResponse> ID = new Type<>(Networking.id("block_highlight_response"));

    public static final StreamCodec<FriendlyByteBuf, BlockHighlightResponse> CODEC = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buf, BlockHighlightResponse value) {
            buf.writeInt(value.positions.size());
            for (BlockPos pos : value.positions) {
                buf.writeLong(pos.asLong());
            }
        }

        @Override
        public BlockHighlightResponse decode(FriendlyByteBuf buf) {
            int size = buf.readInt();
            ArrayList<BlockPos> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                list.add(BlockPos.of(buf.readLong()));
            }
            return new BlockHighlightResponse(list);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
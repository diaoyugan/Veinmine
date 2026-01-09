package top.diaoyugan.vein_mine.networking.highlightingpacket;

import top.diaoyugan.vein_mine.networking.Networking;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public record BlockHighlightResponse(ArrayList<BlockPos> positions) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BlockHighlightResponse> ID = new CustomPacketPayload.Type<>(Networking.id("block_highlight_response"));

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
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
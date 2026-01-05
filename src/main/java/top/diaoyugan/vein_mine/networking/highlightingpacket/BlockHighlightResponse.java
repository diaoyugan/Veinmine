package top.diaoyugan.vein_mine.networking.highlightingpacket;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;


import net.minecraft.util.math.BlockPos;

import top.diaoyugan.vein_mine.networking.Networking;

import java.util.*;


public record BlockHighlightResponse(ArrayList<BlockPos> positions) implements CustomPayload {
    public static final CustomPayload.Id<BlockHighlightResponse> ID = new CustomPayload.Id<>(Networking.id("block_highlight_response"));

    public static final PacketCodec<PacketByteBuf, BlockHighlightResponse> CODEC = new PacketCodec<>() {
        @Override
        public void encode(PacketByteBuf buf, BlockHighlightResponse value) {
            buf.writeInt(value.positions.size());
            for (BlockPos pos : value.positions) {
                buf.writeLong(pos.asLong());
            }
        }

        @Override
        public BlockHighlightResponse decode(PacketByteBuf buf) {
            int size = buf.readInt();
            ArrayList<BlockPos> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                list.add(BlockPos.fromLong(buf.readLong()));
            }
            return new BlockHighlightResponse(list);
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
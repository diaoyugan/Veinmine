package top.diaoyugan.vein_mine.networking.highlightingpacket;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.networking.Networking;

public record BlockHighlightRequest(BlockPos blockPos) implements CustomPayload {
    public static final CustomPayload.Id<BlockHighlightRequest> ID = new CustomPayload.Id<>(Networking.id("block_highlight"));
    public static final PacketCodec<PacketByteBuf, BlockHighlightRequest> CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockHighlightRequest::blockPos, BlockHighlightRequest::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
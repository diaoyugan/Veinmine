package top.diaoyugan.vein_mine.networking.highlightingpacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.diaoyugan.vein_mine.networking.Networking;

public record BlockHighlightRequest(BlockPos blockPos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BlockHighlightRequest> ID = new CustomPacketPayload.Type<>(Networking.id("block_highlight"));
    public static final StreamCodec<FriendlyByteBuf, BlockHighlightRequest> CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, BlockHighlightRequest::blockPos, BlockHighlightRequest::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
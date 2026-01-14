package top.diaoyugan.veinmine.networking.highlightingpacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.diaoyugan.veinmine.networking.Networking;

public record BlockHighlightRequest(BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<BlockHighlightRequest> ID = new Type<>(Networking.id("block_highlight"));
    public static final StreamCodec<FriendlyByteBuf, BlockHighlightRequest> CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, BlockHighlightRequest::blockPos, BlockHighlightRequest::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
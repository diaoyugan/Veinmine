package top.diaoyugan.vein_mine.Networking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

//此为未来可能要用的多端共享连锁高亮准备的代码 就当我在练network通信吧
public class HighlightBlock implements ModInitializer {
    public static final Identifier HIGHLIGHT_PACKET_ID = Networking.id("block_highlight");

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(BlockHighlightPayload.ID, BlockHighlightPayload.CODEC);
    }

    public record BlockHighlightPayload(BlockPos blockPos) implements CustomPayload {
        public static final Id<BlockHighlightPayload> ID = new CustomPayload.Id<>(HighlightBlock.HIGHLIGHT_PACKET_ID);
        public static final PacketCodec<PacketByteBuf, BlockHighlightPayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockHighlightPayload::blockPos, BlockHighlightPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void sendHighlightPacket(ServerPlayerEntity player,BlockPos blockPos) {
        ServerPlayNetworking.send(player, new BlockHighlightPayload(blockPos));
    }
}

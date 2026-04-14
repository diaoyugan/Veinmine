package top.diaoyugan.vein_mine.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomPayload record classes used by the 1.20.6 new-networking-API implementation
 * of {@link ServerNetBridge} / {@link ClientNetBridge}.
 */
public final class Payloads {
    private Payloads() {}

    public record KeyPress() implements CustomPayload {
        public static final KeyPress INSTANCE = new KeyPress();
        public static final CustomPayload.Id<KeyPress> ID = new CustomPayload.Id<>(NetworkIds.KEY_PRESS);
        public static final PacketCodec<PacketByteBuf, KeyPress> CODEC = PacketCodec.unit(INSTANCE);

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record KeyResponse(boolean state) implements CustomPayload {
        public static final CustomPayload.Id<KeyResponse> ID = new CustomPayload.Id<>(NetworkIds.KEY_RESPONSE);
        public static final PacketCodec<PacketByteBuf, KeyResponse> CODEC = new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, KeyResponse value) {
                buf.writeBoolean(value.state);
            }

            @Override
            public KeyResponse decode(PacketByteBuf buf) {
                return new KeyResponse(buf.readBoolean());
            }
        };

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record HighlightRequest(BlockPos pos) implements CustomPayload {
        public static final CustomPayload.Id<HighlightRequest> ID = new CustomPayload.Id<>(NetworkIds.HIGHLIGHT_REQUEST);
        public static final PacketCodec<PacketByteBuf, HighlightRequest> CODEC = new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, HighlightRequest value) {
                buf.writeLong(value.pos.asLong());
            }

            @Override
            public HighlightRequest decode(PacketByteBuf buf) {
                return new HighlightRequest(BlockPos.fromLong(buf.readLong()));
            }
        };

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record HighlightResponse(List<BlockPos> positions) implements CustomPayload {
        public static final CustomPayload.Id<HighlightResponse> ID = new CustomPayload.Id<>(NetworkIds.HIGHLIGHT_RESPONSE);
        public static final PacketCodec<PacketByteBuf, HighlightResponse> CODEC = new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, HighlightResponse value) {
                buf.writeInt(value.positions.size());
                for (BlockPos p : value.positions) {
                    buf.writeLong(p.asLong());
                }
            }

            @Override
            public HighlightResponse decode(PacketByteBuf buf) {
                int n = buf.readInt();
                List<BlockPos> list = new ArrayList<>(n);
                for (int i = 0; i < n; i++) {
                    list.add(BlockPos.fromLong(buf.readLong()));
                }
                return new HighlightResponse(list);
            }
        };

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}

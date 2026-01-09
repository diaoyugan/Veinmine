package top.diaoyugan.vein_mine.networking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class PacketCodecUtil {
    private PacketCodecUtil() {}

    // Unit codec for singleton payloads
    public static <B, P> StreamCodec<B, P> unit(P instance) {
        return StreamCodec.unit(instance);
    }

    // Boolean codec using RegistryByteBuf or PacketByteBuf style buffers.
    public static final StreamCodec<RegistryFriendlyByteBuf, Boolean> BOOLEAN_REGISTRY_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, Boolean value) {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean decode(RegistryFriendlyByteBuf buf) {
            return buf.readBoolean();
        }
    };

    // Generic list codec for PacketByteBuf where elements have their own PacketCodec
    public static <T> StreamCodec<FriendlyByteBuf, List<T>> list(StreamCodec<FriendlyByteBuf, T> elementCodec) {
        return new StreamCodec<>() {
            @Override
            public void encode(FriendlyByteBuf buf, List<T> list) {
                buf.writeInt(list.size());
                for (T el : list) {
                    // elementCodec should know how to write itself into the same buf
                    elementCodec.encode(buf, el);
                }
            }

            @Override
            public List<T> decode(FriendlyByteBuf buf) {
                int size = buf.readInt();
                ArrayList<T> arr = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    arr.add(elementCodec.decode(buf));
                }
                return arr;
            }
        };
    }

    // Helper to convert a simple long-based element codec (like BlockPos) to a PacketCodec that writes/reads longs
    public static <T> StreamCodec<FriendlyByteBuf, T> fromLongCodec(Function<FriendlyByteBuf, T> fromLongFunc, Function<T, Long> toLongFunc) {
        return new StreamCodec<>() {
            @Override
            public void encode(FriendlyByteBuf buf, T value) {
                buf.writeLong(toLongFunc.apply(value));
            }

            @Override
            public T decode(FriendlyByteBuf buf) {
                return fromLongFunc.apply(buf);
            }
        };
    }
}

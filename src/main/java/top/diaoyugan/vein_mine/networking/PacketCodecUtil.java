package top.diaoyugan.vein_mine.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class PacketCodecUtil {
    private PacketCodecUtil() {}

    // Unit codec for singleton payloads
    public static <B, P> PacketCodec<B, P> unit(P instance) {
        return PacketCodec.unit(instance);
    }

    // Boolean codec using RegistryByteBuf or PacketByteBuf style buffers.
    public static final PacketCodec<RegistryByteBuf, Boolean> BOOLEAN_REGISTRY_CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, Boolean value) {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean decode(RegistryByteBuf buf) {
            return buf.readBoolean();
        }
    };

    // Generic list codec for PacketByteBuf where elements have their own PacketCodec
    public static <T> PacketCodec<PacketByteBuf, List<T>> list(PacketCodec<PacketByteBuf, T> elementCodec) {
        return new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, List<T> list) {
                buf.writeInt(list.size());
                for (T el : list) {
                    // elementCodec should know how to write itself into the same buf
                    elementCodec.encode(buf, el);
                }
            }

            @Override
            public List<T> decode(PacketByteBuf buf) {
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
    public static <T> PacketCodec<PacketByteBuf, T> fromLongCodec(Function<PacketByteBuf, T> fromLongFunc, Function<T, Long> toLongFunc) {
        return new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, T value) {
                buf.writeLong(toLongFunc.apply(value));
            }

            @Override
            public T decode(PacketByteBuf buf) {
                return fromLongFunc.apply(buf);
            }
        };
    }
}

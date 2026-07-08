package top.diaoyugan.veinmine.fabric.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class PayloadRegistrar {
    private PayloadRegistrar() {}

    public static <T extends CustomPacketPayload> void registerServerboundPlay(CustomPacketPayload.Type<T> id, StreamCodec<FriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.serverboundPlay().register(id, codec);
    }

    public static <T extends CustomPacketPayload> void registerClientboundPlay(CustomPacketPayload.Type<T> id, StreamCodec<FriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.clientboundPlay().register(id, codec);
    }
    public static void init() {
        NetPacketsRegistrar.init();
    }
}

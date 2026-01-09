package top.diaoyugan.vein_mine.networking;

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

    // 初始化入口（方便未来统一调用）
    public static void init() {
        // 在 mod 初始化处调用各模块的 onInitialize / 注册函数
        NetPacketsRegistrar.init();
        // 如果有其它 payloads，后续在这里统一初始化
    }
}

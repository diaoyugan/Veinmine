package top.diaoyugan.vein_mine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class PayloadRegistrar {
    private PayloadRegistrar() {}

    public static <T extends CustomPayload> void registerPlayC2S(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec) {
        PayloadTypeRegistry.playC2S().register(id, codec);
    }

    public static <T extends CustomPayload> void registerPlayS2C(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(id, codec);
    }

    // 初始化入口（方便未来统一调用）
    public static void init() {
        // 在 mod 初始化处调用各模块的 onInitialize / 注册函数
        NetPacketsRegistrar.init();
        // 如果有其它 payloads，后续在这里统一初始化
    }
}

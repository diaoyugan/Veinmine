package top.diaoyugan.vein_mine.network.keybindreciever;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class NetworkingKeyBindPacket implements ModInitializer {
    public static void receive(KeybindPayload payload, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> context.player().sendMessage(Text.literal("So you pressed ").append(Text.keybind("key.vein_mine.activate").styled(style -> style.withFormatting(Formatting.BLUE))), false));
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);
        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, KeybindPayload.ID, NetworkingKeyBindPacket::receive));
    }
}

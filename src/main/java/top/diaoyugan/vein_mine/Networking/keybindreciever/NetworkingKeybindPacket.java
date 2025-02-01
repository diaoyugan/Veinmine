
package top.diaoyugan.vein_mine.Networking.keybindreciever;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.Utils;

public final class NetworkingKeybindPacket implements ModInitializer {

	// 接收按键包的逻辑
	private static void receive(KeybindPayload payload, ServerPlayNetworking.Context context) {
		// 切换状态
		boolean currentState = Utils.toggleVeinMineSwitchState();
		Text message = Text.translatable("vm.switch_state").append(": ")
				.append(Text.translatable(currentState ? "options.on" : "options.off").styled(style -> style.withFormatting(currentState ? Formatting.GREEN : Formatting.RED)));
		Messages.sendMessage(context.player(), message,true);
	}

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);
		ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, KeybindPayload.ID, NetworkingKeybindPacket::receive));
	}
}

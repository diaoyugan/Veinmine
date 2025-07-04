
package top.diaoyugan.vein_mine.networking.keybindreciever;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.Utils;

public class NetworkingKeybindingPacket implements ModInitializer {

	// 接收按键包的逻辑
	private static void receive(KeybindingPayload payload, ServerPlayNetworking.Context context) {
		// 切换状态
		boolean currentState = Utils.toggleVeinMineSwitchState(context.player());
		Text message = Text.translatable("vm.switch_state").append(": ")
				.append(Text.translatable(currentState ? "options.on" : "options.off")
						.styled(style -> style.withFormatting(currentState ? Formatting.GREEN : Formatting.RED)));

		// 向客户端发送当前状态的消息
		Messages.sendMessage(context.player(), message, true);

		// 发送新的状态给客户端
		ServerPlayNetworking.send(context.player(), new KeybindingPayloadResponse(currentState));
	}

	@Override
	public void onInitialize() {
		// 注册 KeybindingPayload 类型用于接收客户端的请求
		PayloadTypeRegistry.playC2S().register(KeybindingPayload.ID, KeybindingPayload.CODEC);

		// 注册 KeybindingPayloadResponse 类型用于客户端接收返回的状态
		PayloadTypeRegistry.playS2C().register(KeybindingPayloadResponse.ID, KeybindingPayloadResponse.CODEC);

		ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, KeybindingPayload.ID, NetworkingKeybindingPacket::receive));
	}
}

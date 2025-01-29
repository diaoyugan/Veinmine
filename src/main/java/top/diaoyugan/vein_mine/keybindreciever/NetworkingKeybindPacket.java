
package top.diaoyugan.vein_mine.keybindreciever;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


public final class NetworkingKeybindPacket implements ModInitializer {

	// 全局开关状态
	private static boolean switchState = false; // 初始为关

	// 切换开关状态的方法
	public static boolean toggleSwitchState() {
		switchState = !switchState;
		return switchState; // 返回当前状态（方便其他地方获取）
	}

	// 获取当前开关状态
	public static boolean getSwitchState() {
		return switchState;
	}

	// 接收按键包的逻辑
	private static void receive(KeybindPayload payload, ServerPlayNetworking.Context context) {
		// 切换状态
		boolean currentState = toggleSwitchState();

		// 创建文本消息
		Text message = Text.literal("So you pressed ")
				.append(Text.literal("key.vm.switch").styled(style -> style.withFormatting(Formatting.BLUE)))
				.append(Text.literal(" The switch is now: "))
				.append(Text.literal(currentState ? "ON" : "OFF").styled(style -> style.withFormatting(currentState ? Formatting.GREEN : Formatting.RED)));

		// 发送消息到玩家
		context.player().server.execute(() -> context.player().sendMessage(message, false));
	}

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);
		ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, KeybindPayload.ID, NetworkingKeybindPacket::receive));
	}
}

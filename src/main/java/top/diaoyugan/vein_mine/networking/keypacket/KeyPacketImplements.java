
package top.diaoyugan.vein_mine.networking.keypacket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.Utils;

public class KeyPacketImplements implements ModInitializer {

    // 接收按键包的逻辑
    private static void receive(KeyPressPacket payload, ServerPlayNetworking.Context context) {
        // 切换状态
        boolean currentState = Utils.toggleVeinMineSwitchState(context.player());
        Component message = Component.translatable("vm.switch_state").append(": ")
                .append(Component.translatable(currentState ? "options.on" : "options.off")
                        .withStyle(style -> style.applyFormat(currentState ? ChatFormatting.GREEN : ChatFormatting.RED)));

        // 向客户端发送当前状态的消息
        Messages.sendMessage(context.player(), message, true);

        // 发送新的状态给客户端
        ServerPlayNetworking.send(context.player(), new KeyResponsePacket(currentState));
    }

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, KeyPressPacket.ID, KeyPacketImplements::receive));
    }
}

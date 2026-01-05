
package top.diaoyugan.vein_mine.networking.keypacket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.Utils;

public class KeyPacketImplements implements ModInitializer {

    // 接收按键包的逻辑
    private static void receive(KeyPressPacket payload, ServerPlayNetworking.Context context) {
        // 切换状态
        boolean currentState = Utils.toggleVeinMineSwitchState(context.player());
        Text message = Text.translatable("vm.switch_state").append(": ")
                .append(Text.translatable(currentState ? "options.on" : "options.off")
                        .styled(style -> style.withFormatting(currentState ? Formatting.GREEN : Formatting.RED)));

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

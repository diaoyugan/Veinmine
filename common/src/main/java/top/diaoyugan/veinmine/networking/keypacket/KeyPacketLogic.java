package top.diaoyugan.veinmine.networking.keypacket;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.utils.Messages;
import top.diaoyugan.veinmine.utils.Utils;


public final class KeyPacketLogic {

    private KeyPacketLogic() {}

    /**
     * @param player 服务端玩家
     * @param sendState 回传新状态给客户端（由加载器实现）
     */
    public static void handleKeyPress(
            ServerPlayer player,
            BooleanConsumer sendState
    ) {
        boolean currentState = Utils.toggleVeinMineSwitchState(player);

        Component message = Component.translatable("vm.switch_state")
                .append(": ")
                .append(Component.translatable(currentState ? "options.on" : "options.off")
                        .withStyle(style ->
                                style.applyFormat(
                                        currentState ? ChatFormatting.GREEN : ChatFormatting.RED
                                )));

        Messages.sendMessage(player, message, true);

        sendState.accept(currentState);
    }
}

package top.diaoyugan.veinmine.neoforge.networking.keypacket;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.hotkey.ActivationKeyState;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketLogic;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;


@EventBusSubscriber(modid = Constants.ID)
public final class KeyPacketImplements {
    @SubscribeEvent()
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");
        registrar.playToClient(
                KeyResponsePacket.ID,
                KeyResponsePacket.CODEC,
                (msg, ctx) -> ActivationKeyState.updateFromServer(msg.state())
        );
        registrar.playToServer(
                KeyPressPacket.ID,
                KeyPressPacket.CODEC,
                (payload, ctx) -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();

                    KeyPacketLogic.handleKeyPress(
                            player,
                            state -> ctx.reply(
                                    new KeyResponsePacket(state)
                            )
                    );
                }
        );
    }
}

package top.diaoyugan.veinmine;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.client.hotkey.HotKeyState;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketLogic;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;


@Mod(Constants.ID)
@EventBusSubscriber(modid = Constants.ID)
public class Veinmine {
    /**这地方大概类似于fabric的入口点 但必须全部写进这个方法里 这个类里必须要有同名的public方法*/
    public Veinmine(ModContainer container) {
    }
    @SubscribeEvent()
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");
        registrar.playToClient(
                KeyResponsePacket.ID,
                KeyResponsePacket.CODEC,
                (msg, ctx) -> HotKeyState.updateFromServer(msg.state())
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

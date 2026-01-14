package top.diaoyugan.veinmine.client;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.hotkey.HotKeyState;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketLogic;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;

@EventBusSubscriber(modid = Constants.ID, value = Dist.CLIENT)
public final class VeinmineClient {

    private VeinmineClient() {}
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");

        // 服务端接收 KeyPressPacket
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
        registrar.playToClient(
                KeyResponsePacket.ID,
                KeyResponsePacket.CODEC,
                (msg, ctx) -> HotKeyState.updateFromServer(msg.state())
        );

    }
    @SubscribeEvent
    public static void onEndClientTick(ClientTickEvent.Post event) {
        HotKeys.tickEvent(Minecraft.getInstance());
    }
}


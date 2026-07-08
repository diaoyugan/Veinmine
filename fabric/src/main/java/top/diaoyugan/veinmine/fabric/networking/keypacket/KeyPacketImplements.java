package top.diaoyugan.veinmine.fabric.networking.keypacket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketLogic;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;

public class KeyPacketImplements implements ModInitializer {

    private static void receive(
            KeyPressPacket payload,
            ServerPlayNetworking.Context context
    ) {
        KeyPacketLogic.handleKeyPress(
                context.player(),
                state -> ServerPlayNetworking.send(
                        context.player(),
                        new KeyResponsePacket(state)
                )
        );
    }

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.INIT.register(
                (handler, server) ->
                        ServerPlayNetworking.registerReceiver(
                                handler,
                                KeyPressPacket.ID,
                                KeyPacketImplements::receive
                        )
        );
    }
}

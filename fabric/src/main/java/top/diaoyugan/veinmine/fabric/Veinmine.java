package top.diaoyugan.veinmine.fabric;

import net.fabricmc.api.ModInitializer;

import top.diaoyugan.veinmine.fabric.events.PlayerBreakBlock;
import top.diaoyugan.veinmine.fabric.events.PlayerDisconnect;
import top.diaoyugan.veinmine.fabric.networking.PayloadRegistrar;
import top.diaoyugan.veinmine.fabric.networking.keypacket.KeyPacketImplements;


public class Veinmine implements ModInitializer {

    @Override
    public void onInitialize() {
        // 娉ㄥ唽
        PlayerBreakBlock.register();
        new KeyPacketImplements().onInitialize();
        PayloadRegistrar.init();
        PlayerDisconnect.register();
        FabricCommandRegister.register();
    }

}


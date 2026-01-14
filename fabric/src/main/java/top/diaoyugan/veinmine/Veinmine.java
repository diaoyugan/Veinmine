package top.diaoyugan.veinmine;

import net.fabricmc.api.ModInitializer;

import top.diaoyugan.veinmine.events.PlayerBreakBlock;
import top.diaoyugan.veinmine.events.PlayerDisconnect;
import top.diaoyugan.veinmine.networking.PayloadRegistrar;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketImplements;


public class Veinmine implements ModInitializer {

    @Override
    public void onInitialize() {
        // 注册
        PlayerBreakBlock.register();
        new KeyPacketImplements().onInitialize();
        PayloadRegistrar.init();
        PlayerDisconnect.register();
    }

}


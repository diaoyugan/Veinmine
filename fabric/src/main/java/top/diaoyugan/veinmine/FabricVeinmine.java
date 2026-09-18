package top.diaoyugan.veinmine;

import net.fabricmc.api.ModInitializer;

import top.diaoyugan.veinmine.events.FabricPlayerBreakBlock;
import top.diaoyugan.veinmine.events.FabricPlayerDisconnect;
import top.diaoyugan.veinmine.networking.PayloadRegistrar;
import top.diaoyugan.veinmine.networking.keypacket.FabricKeyPacketImplements;


public class FabricVeinmine implements ModInitializer {

    @Override
    public void onInitialize() {
        // 注册
        FabricPlayerBreakBlock.register();
        new FabricKeyPacketImplements().onInitialize();
        PayloadRegistrar.init();
        FabricPlayerDisconnect.register();
        FabricCommandRegister.register();
    }

}


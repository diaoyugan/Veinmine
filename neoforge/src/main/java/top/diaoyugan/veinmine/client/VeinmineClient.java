package top.diaoyugan.veinmine.client;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.configScreen.ConfigScreen;
import top.diaoyugan.veinmine.client.hotkey.HotKeyState;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketLogic;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;

@EventBusSubscriber(modid = Constants.ID, value = Dist.CLIENT)
public final class VeinmineClient {

    private VeinmineClient() {}
    @SubscribeEvent
    public static void onEndClientTick(ClientTickEvent.Post event) {
        HotKeys.tickEvent(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event){
        ModContainer container = event.getContainer();
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (modContainer, parent) -> new ConfigScreen(parent)
        );
    }
}


package top.diaoyugan.veinmine.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.configScreen.ConfigScreen;
import top.diaoyugan.veinmine.client.inputs.KeyHandler;
import top.diaoyugan.veinmine.client.inputs.NeoKeyHandler;
import top.diaoyugan.veinmine.networking.MiningPreferencesPacket;

@EventBusSubscriber(modid = Constants.ID, value = Dist.CLIENT)
public final class
VeinmineClient {

    private VeinmineClient() {}
    @SubscribeEvent
    public static void onEndClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        ClientMiningPreferences.sync(client, connection -> connection.hasChannel(MiningPreferencesPacket.ID),
                ClientPacketDistributor::sendToServer);
        NeoKeyHandler.tickEvent(client);
        KeyHandler.clientTick(client);
    }

    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event){
        ModContainer container = event.getContainer();
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (modContainer, parent) -> new ConfigScreen(parent)
        );
    }
}


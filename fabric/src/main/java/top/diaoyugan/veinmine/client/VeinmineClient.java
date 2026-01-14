package top.diaoyugan.veinmine.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.veinmine.client.keybinding.FabricKeyBinding;
import top.diaoyugan.veinmine.client.render.CustomLayers;
import top.diaoyugan.veinmine.client.render.FabricOutlineRenderHook;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;

public class VeinmineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        FabricHighlightNetworking.onInitialize();
        ClientTickEvents.END_CLIENT_TICK.register(HotKeys::tickEvent);

        FabricKeyBinding.onInitialize();
        FabricOutlineRenderHook.init();


        ClientPlayNetworking.registerGlobalReceiver(KeyResponsePacket.ID, HotKeys::receiveKeybindingResponse);
    }
}

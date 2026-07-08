package top.diaoyugan.veinmine.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.veinmine.fabric.client.inputs.FabricKeyHandler;
import top.diaoyugan.veinmine.client.inputs.KeyHandler;
import top.diaoyugan.veinmine.fabric.client.keybinding.FabricKeyBinding;
import top.diaoyugan.veinmine.fabric.client.render.FabricOutlineRenderHook;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;

public class VeinmineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        FabricHighlightNetworking.onInitialize();
        ClientTickEvents.END_CLIENT_TICK.register(FabricKeyHandler::tickEvent);
        ClientTickEvents.END_CLIENT_TICK.register(KeyHandler::clientTick);

        FabricKeyBinding.onInitialize();
        FabricOutlineRenderHook.init();


        ClientPlayNetworking.registerGlobalReceiver(KeyResponsePacket.ID, FabricKeyHandler::receiveKeybindingResponse);
    }
}

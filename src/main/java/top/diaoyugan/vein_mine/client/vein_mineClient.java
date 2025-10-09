package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.vein_mine.networking.keybindreciever.KeybindingPayloadResponse;

public class vein_mineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientBlockHighlighting.onInitialize();
        ClientTickEvents.END_CLIENT_TICK.register(HotKeys::tickEvent);

        ClientVersionInterface vCLInitialize = new CLInterfaceOverride();
        vCLInitialize.OnInitialize();

        ClientPlayNetworking.registerGlobalReceiver(KeybindingPayloadResponse.ID, HotKeys::receiveKeybindingResponse);
    }
}

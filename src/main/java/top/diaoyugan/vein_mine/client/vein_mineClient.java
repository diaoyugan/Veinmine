package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import top.diaoyugan.vein_mine.networking.ClientNetBridge;

public class vein_mineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetBridge.INSTANCE.registerReceivers();
        ClientTickEvents.END_CLIENT_TICK.register(HotKeys::tickEvent);

        ClientVersionInterface CLI = new CLInterfaceOverride();
        CLI.OnInitialize();
    }
}

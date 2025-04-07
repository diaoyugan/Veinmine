
package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindingPayloadResponse;

public class vein_mineClient implements ClientModInitializer {
    protected static final KeyBinding BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.vm.switch",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "key.category.vm.switch"));

    @Override
    public void onInitializeClient() {
        ClientBlockHighlighting.onInitialize();

        ClientTickEvents.END_CLIENT_TICK.register(HotKeys::tickEvent);
        ClientInitialize versions = new VersionInit();
        versions.OnInitialize();
        // 注册接收来自服务端的状态更新
        ClientPlayNetworking.registerGlobalReceiver(KeybindingPayloadResponse.ID, HotKeys::receiveKeybindingResponse);
    }

}


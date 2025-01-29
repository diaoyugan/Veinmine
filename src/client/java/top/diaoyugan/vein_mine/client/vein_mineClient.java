
package top.diaoyugan.vein_mine.client;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.vein_mine.keybindreciever.KeybindPayload;

// Sends a packet to the server when a keybinding was pressed
// The server in response will send a chat message to the client.
public class vein_mineClient implements ClientModInitializer {
    public static final KeyBinding TEST_BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.vm.switch",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "key.category.vm.switch"));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Player must be in game to send packets, i.e. client.player != null
            if (client.getNetworkHandler() != null) {
                if (TEST_BINDING.wasPressed()) {
                    // Send an empty payload, server just needs to be told when packet is sent
                    // Since KeybindPayload is an empty payload, it can be a singleton.
                    ClientPlayNetworking.send(KeybindPayload.INSTANCE);
                }
            }
        });
    }
}

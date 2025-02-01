
package top.diaoyugan.vein_mine.client;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.vein_mine.InvisibleBlock;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindPayload;
import top.diaoyugan.vein_mine.utils.Utils;


public class vein_mineClient implements ClientModInitializer {
    public static final KeyBinding TEST_BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.vm.switch",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "key.category.vm.switch"));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.getNetworkHandler() != null) {
                if (client.player != null && Utils.getVeinMineSwitchState(client.player)) {
                    if (client.player != null) {
                        ClientBlockHighlighting.checkPlayerLooking(client.player);
                    }
                }
                if (TEST_BINDING.wasPressed()) {
                    ClientPlayNetworking.send(KeybindPayload.INSTANCE);
                }
            }
        });
    }
}
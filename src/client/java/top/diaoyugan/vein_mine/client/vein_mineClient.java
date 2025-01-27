package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import top.diaoyugan.vein_mine.network.keybindreciever.*;
import org.lwjgl.glfw.GLFW;


public class vein_mineClient implements ClientModInitializer {

    private static final KeyBinding KEY_BINDING = new KeyBinding(
            "key.vein_mine.activate",  // 描述键位的文本
            InputUtil.Type.KEYSYM,     // 键盘按键
            GLFW.GLFW_KEY_GRAVE_ACCENT,          // 默认的按键
            "category.vein_mine"      // 键位分类（用于在设置中归类）
    );

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KEY_BINDING);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Player must be in game to send packets, i.e. client.player != null
            if (client.getNetworkHandler() != null) {
                if (KEY_BINDING.wasPressed()) {
                    // Send an empty payload, server just needs to be told when packet is sent
                    // Since KeybindPayload is an empty payload, it can be a singleton.
                    ClientPlayNetworking.send(KeybindPayload.INSTANCE);
                }
            }
        });
    }
}



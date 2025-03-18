
package top.diaoyugan.vein_mine.client;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindPayload;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindPayloadResponse;
import top.diaoyugan.vein_mine.client.render.RenderOutlines;

public class vein_mineClient implements ClientModInitializer {
    public static final KeyBinding BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.vm.switch",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "key.category.vm.switch"));

    private static boolean veinMineSwitchState = false;  // 用来保存从服务器接收到的状态

    @Override
    public void onInitializeClient() {
        ClientBlockHighlighting.onInitialize();
        RenderOutlines.onInitialize();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.getNetworkHandler() != null) {
                if (client.player != null && veinMineSwitchState) {
                    ClientBlockHighlighting.checkPlayerLooking(client.player);
                }

                // 按下切换按键时，发送请求包
                if (BINDING.wasPressed()) {
                    ClientPlayNetworking.send(KeybindPayload.INSTANCE);
                }
            }

            if (client.player != null && !veinMineSwitchState) {
                ClientBlockHighlighting.HIGHLIGHTED_BLOCKS.clear();
            }
        });

        // 注册接收来自服务端的状态更新
        ClientPlayNetworking.registerGlobalReceiver(KeybindPayloadResponse.ID, vein_mineClient::receiveKeybindResponse);
    }

    // 接收服务端返回的开关状态
    private static void receiveKeybindResponse(KeybindPayloadResponse response, ClientPlayNetworking.Context context) {
        veinMineSwitchState = response.state();  // 更新客户端的开关状态
    }
}


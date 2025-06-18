package top.diaoyugan.vein_mine.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import top.diaoyugan.vein_mine.config.Config;
import top.diaoyugan.vein_mine.Networking.keybindreciever.KeybindingPayloadResponse;

public class vein_mineClient implements ClientModInitializer {
    public static KeyBinding BINDING;

    @Override
    public void onInitializeClient() {
        registerKeyBindingFromConfig();

        ClientBlockHighlighting.onInitialize();
        ClientTickEvents.END_CLIENT_TICK.register(HotKeys::tickEvent);

        ClientVersionInterface vCLInitialize = new ClientVersionOnInitialize();
        vCLInitialize.OnInitialize();

        ClientPlayNetworking.registerGlobalReceiver(KeybindingPayloadResponse.ID, HotKeys::receiveKeybindingResponse);
    }

    public static void registerKeyBindingFromConfig() {
        int keyCode = Config.getInstance().getConfigItems().keyBindingCode;
        InputUtil.Key key = InputUtil.fromKeyCode(keyCode, 0);

        BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vm.switch",
                key.getCode(),
                "key.category.vm.switch"
        ));
    }

    public static void updateKeyBinding(int newCode) {
        BINDING.setBoundKey(InputUtil.fromKeyCode(newCode, 0));
        // 强制保存新的按键设置
        MinecraftClient.getInstance().options.write();
        // 更新按键绑定 没有这一行会导致游戏内不会立刻生效
        KeyBinding.updateKeysByCode();
    }


}

package top.diaoyugan.vein_mine.client.keybinding;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import top.diaoyugan.vein_mine.config.Config;
import net.minecraft.client.option.KeyBinding.Category;

public class KeyBinding {
    public static void onInitialize(){
        registerKeyBindingFromConfig();
    }

    public static net.minecraft.client.option.KeyBinding BINDING;
    public static final Category VM_CATEGORY = Category.create(Identifier.of("key.category.vm.switch"));

    public static void registerKeyBindingFromConfig() {
        int keyCode = Config.getInstance().getConfigItems().keyBindingCode;
        InputUtil.Key key = InputUtil.fromKeyCode(new KeyInput(keyCode, 0, 0));


        BINDING = KeyBindingHelper.registerKeyBinding(new net.minecraft.client.option.KeyBinding(
                "key.vm.switch",
                key.getCode(),
                VM_CATEGORY
        ));
    }

    public static void updateKeyBinding(int newCode) {
        BINDING.setBoundKey(InputUtil.fromKeyCode(new KeyInput(newCode, 0, 0)));
        // 强制保存新的按键设置
        MinecraftClient.getInstance().options.write();
        // 更新按键绑定 没有这一行会导致游戏内不会立刻生效
        net.minecraft.client.option.KeyBinding.updateKeysByCode();
    }
}

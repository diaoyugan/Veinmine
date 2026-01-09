package top.diaoyugan.vein_mine.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.vein_mine.config.Config;

public class KeyBinding {
    public static void onInitialize(){
        registerKeyBindingFromConfig();
    }

    public static net.minecraft.client.KeyMapping BINDING;
    public static final Category VM_CATEGORY = Category.register(Identifier.parse("key.category.vm.switch"));

    public static void registerKeyBindingFromConfig() {
        int keyCode = Config.getInstance().getConfigItems().keyBindingCode;
        InputConstants.Key key = InputConstants.getKey(new KeyEvent(keyCode, 0, 0));


        BINDING = KeyBindingHelper.registerKeyBinding(new net.minecraft.client.KeyMapping(
                "key.vm.switch",
                key.getValue(),
                VM_CATEGORY
        ));
    }

    public static void updateKeyBinding(int newCode) {
        BINDING.setKey(InputConstants.getKey(new KeyEvent(newCode, 0, 0)));
        // 强制保存新的按键设置
        Minecraft.getInstance().options.save();
        // 更新按键绑定 没有这一行会导致游戏内不会立刻生效
        net.minecraft.client.KeyMapping.resetMapping();
    }

    // 提供默认键
    public static InputConstants.Key getDefaultKey() {
        return InputConstants.getKey(new KeyEvent(GLFW.GLFW_KEY_GRAVE_ACCENT, 0, 0));
    }

    // 根据 keyCode 提供对应键
    public static InputConstants.Key getConfigKey(int keyCode) {
        return InputConstants.getKey(new KeyEvent(keyCode, 0, 0));
    }
}

package top.diaoyugan.veinmine.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.config.Config;

public class KeyBinding {
    //mojang小时候出生取名叫mojang但是强制继承其父姓于是叫做Markus.Persson.Mojang.Studios
    //后来被微软收购了 现在叫做Microsoft.Xbox.Game.Studios.Mojang.Studios
    //什么傻逼会没事干给你的命名强制加前缀啊？？
    public static final KeyMapping.Category VM_CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(
                            Constants.ID,"switch"
                    ))
            ;
    public static net.minecraft.client.KeyMapping BINDING;


    /** 默认键 */
    public static int defaultKey() {
        return GLFW.GLFW_KEY_GRAVE_ACCENT;
    }

    /** 配置里的键 */
    public static int configuredKey() {
        return Config.getInstance().getConfigItems().keyBindingCode;
    }

    public static void updateKeyBinding(int newCode) {
        KeyBinding.BINDING.setKey(InputConstants.getKey(new KeyEvent(newCode, 0, 0)));
        // 强制保存新的按键设置
        Minecraft.getInstance().options.save();
        // 更新按键绑定 没有这一行会导致游戏内不会立刻生效
        net.minecraft.client.KeyMapping.resetMapping();
    }
}

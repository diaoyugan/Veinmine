package top.diaoyugan.veinmine.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.veinmine.Constants;

public class KeyBinding {
    public static final KeyMapping.Category VM_CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(
                            Constants.ID,"keys"
                    )
            );
    public static net.minecraft.client.KeyMapping BINDING;


    /** 默认键 */
    public static int defaultKey() {
        return GLFW.GLFW_KEY_GRAVE_ACCENT;
    }
}

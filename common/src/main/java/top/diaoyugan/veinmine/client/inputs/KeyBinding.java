package top.diaoyugan.veinmine.client.inputs;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import top.diaoyugan.veinmine.Constants;

public class KeyBinding {
    public static final KeyMapping.Category VM_CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(
                            Constants.ID,"bindings"
                    )
            );
    public static KeyMapping ACTIVATION_KEY;


    /** 默认键 */
    public static int defaultActivationKey() {
        return GLFW.GLFW_KEY_GRAVE_ACCENT;
    }
}

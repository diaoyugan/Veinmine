package top.diaoyugan.veinmine.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.input.KeyEvent;
import top.diaoyugan.veinmine.client.KeyBinding;
import top.diaoyugan.veinmine.config.Config;

public class FabricKeyBinding {
    public static void onInitialize(){
        registerKeyBindingFromConfig();
    }

    public static void registerKeyBindingFromConfig() {
        int keyCode = Config.getInstance().getConfigItems().keyBindingCode;
        InputConstants.Key key = InputConstants.getKey(new KeyEvent(keyCode, 0, 0));


        KeyBinding.BINDING = KeyMappingHelper.registerKeyMapping(new net.minecraft.client.KeyMapping(
                "key.vm.switch",
                key.getValue(),
                KeyBinding.VM_CATEGORY
        ));
    }

}

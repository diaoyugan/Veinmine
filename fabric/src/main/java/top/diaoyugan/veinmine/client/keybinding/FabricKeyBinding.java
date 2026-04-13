package top.diaoyugan.veinmine.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;
import top.diaoyugan.veinmine.client.KeyBinding;

public class FabricKeyBinding {
    public static void onInitialize(){
        registerKeyBinding();
    }

    public static void registerKeyBinding() {
        int keyCode = KeyBinding.defaultActivationKey();
        InputConstants.Key key = InputConstants.getKey(new KeyEvent(keyCode, 0, 0));


        KeyBinding.ACTIVATION_KEY = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                "key.vm.switch",
                key.getValue(),
                KeyBinding.VM_CATEGORY
        ));


    }

}

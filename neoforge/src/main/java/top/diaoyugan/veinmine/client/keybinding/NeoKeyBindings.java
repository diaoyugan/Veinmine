package top.diaoyugan.veinmine.client.keybinding;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.client.KeyBinding;
import top.diaoyugan.veinmine.config.Config;

@EventBusSubscriber(modid = Constants.ID,value = Dist.CLIENT)
public final class NeoKeyBindings {

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        int keyCode = Config.getInstance().getConfigItems().keyBindingCode;

        KeyBinding.BINDING = new KeyMapping(
                "key.vm.switch",
                keyCode,
                KeyBinding.VM_CATEGORY
        );

        event.register(KeyBinding.BINDING);
    }
}


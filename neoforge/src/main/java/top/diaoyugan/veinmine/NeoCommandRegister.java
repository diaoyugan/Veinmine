package top.diaoyugan.veinmine;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import top.diaoyugan.veinmine.utils.Command;

@EventBusSubscriber(modid = Constants.ID)
public class NeoCommandRegister {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Command.registerAll(event.getDispatcher());
    }
}

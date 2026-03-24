package top.diaoyugan.veinmine;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import top.diaoyugan.veinmine.utils.Command;

public class NeoCommandRegister {
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        Command.registerAll(event.getDispatcher());
    }
}

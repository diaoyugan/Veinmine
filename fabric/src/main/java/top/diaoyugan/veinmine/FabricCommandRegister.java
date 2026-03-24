package top.diaoyugan.veinmine;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import top.diaoyugan.veinmine.utils.Command;

public class FabricCommandRegister {
    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Command.registerAll(dispatcher);
        });
    }
}

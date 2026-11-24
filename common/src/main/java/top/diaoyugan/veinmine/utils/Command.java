package top.diaoyugan.veinmine.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandPermissionLevels;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.utils.logging.Logger;
import top.diaoyugan.veinmine.utils.logging.LoggerLevels;


public class Command {
    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("veinmine")
                .requires(Commands.hasPermission(CommandPermissionLevels.LEVEL_ADMINS))
                .then(Commands.literal("reload")
                        .requires(Commands.hasPermission(CommandPermissionLevels.LEVEL_ADMINS))
                        .executes(Command::reload)));
    }


    private static int reload(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try{
            Config.getInstance().load();
            context.getSource().sendSuccess(() -> Component.translatable("vm.command.reload"), true);
            return 1;
        } catch (Exception e){
            Logger.throwLog(LoggerLevels.ERROR,"An error occurred when reloading VeinMine configs:",e);
            throw new SimpleCommandExceptionType(
                    Component.translatable("vm.command.reload_failed")
            ).create();
        }
    }
}

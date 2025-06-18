package top.diaoyugan.vein_mine.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class versionMessage implements utilsVersionInterface{
    @Override
    public void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        if (isOnActionbar)
            Objects.requireNonNull(player.getServer()).execute(() -> player.sendMessage(message, true));
        else
            Objects.requireNonNull(player.getServer()).execute(() -> player.sendMessage(message, false));
    }
}

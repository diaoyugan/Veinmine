package top.diaoyugan.vein_mine.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class MessagesOverride {
    public static void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        if (isOnActionbar)
            Objects.requireNonNull(player.getEntityWorld().getServer()).execute(() -> player.sendMessage(message, true));
        else
            Objects.requireNonNull(player.getEntityWorld().getServer()).execute(() -> player.sendMessage(message, false));
    }
}

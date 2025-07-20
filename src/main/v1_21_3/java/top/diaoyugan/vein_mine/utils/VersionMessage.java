package top.diaoyugan.vein_mine.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class VersionMessage implements IUtilsVersion {
    @Override
    public void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        if (isOnActionbar)
            player.server.execute(() -> player.sendMessage(message, true));
        else
            player.server.execute(() -> player.sendMessage(message, false));
    }
}

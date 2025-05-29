package top.diaoyugan.vein_mine.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface utilsVersionInterface {
    void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar);
}

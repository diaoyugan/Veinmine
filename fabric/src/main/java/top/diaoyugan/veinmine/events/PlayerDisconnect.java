package top.diaoyugan.veinmine.events;

import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.UUID;

public class PlayerDisconnect {
    public static void register(){
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
        ServerPlayer player = handler.player;
        if (player != null) {
            UUID playerId = player.getUUID();
            Utils.clearVeinMineState(playerId);
        }
        });
    }
}

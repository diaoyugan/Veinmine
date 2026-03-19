package top.diaoyugan.veinmine.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.networking.NetPacketsRegistrar;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.UUID;

public class PlayerDisconnect {
    public static void register(){
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
        ServerPlayer player = handler.player;
        if (player != null) {
            UUID playerId = player.getUUID();
            Utils.clearVeinMineState(playerId);
            NetPacketsRegistrar.clearLastStates(playerId);
        }
        });
    }
}

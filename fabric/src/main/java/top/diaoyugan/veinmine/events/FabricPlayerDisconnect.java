package top.diaoyugan.veinmine.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.config.ServerMiningRules;
import top.diaoyugan.veinmine.networking.NetPacketsRegistrar;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.UUID;

public class FabricPlayerDisconnect {
    public static void register(){
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
        ServerPlayer player = handler.player;
        if (player != null) {
            UUID playerId = player.getUUID();
            Utils.clearVeinMineState(playerId);
            ServerMiningRules.clearPreferences(playerId);
            NetPacketsRegistrar.clearLastStates(playerId);
        }
        });
    }
}

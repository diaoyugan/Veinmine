package top.diaoyugan.veinmine.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.networking.ServerMiningNetworking;

public class FabricPlayerDisconnect {
    public static void register(){
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayer player = handler.player;
            if (player != null) ServerMiningNetworking.onPlayerDisconnect(player.getUUID());
        });
    }
}

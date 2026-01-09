package top.diaoyugan.vein_mine;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.vein_mine.networking.PayloadRegistrar;
import top.diaoyugan.vein_mine.networking.keypacket.KeyPacketImplements;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.UUID;


public class vein_mine implements ModInitializer {
    public static final String ID = "vein_mine";

    @Override
    public void onInitialize() {
        // 注册
        PlayerBreakBlock.register();
        new KeyPacketImplements().onInitialize();
        PayloadRegistrar.init();

        //TODO:把这玩意换个地方放
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayer player = handler.player;
            if (player != null) {
                UUID playerId = player.getUUID();
                Utils.clearVeinMineState(playerId);
            }
        });

    }


}

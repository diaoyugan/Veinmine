package top.diaoyugan.vein_mine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import top.diaoyugan.vein_mine.networking.HighlightBlock;
import top.diaoyugan.vein_mine.networking.keybindreciever.NetworkingKeybindingPacket;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.UUID;


public class vein_mine implements ModInitializer {
    public static final String ID = "vein_mine";

    @Override
    public void onInitialize() {
        // 注册
        PlayerBreakBlock.register();
        new NetworkingKeybindingPacket().onInitialize();
        new HighlightBlock().onInitialize();
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            if (player != null) {
                UUID playerId = player.getUuid();
                Utils.clearVeinMineState(playerId);
            }
        });
    }
}

package top.diaoyugan.veinmine.neoforge.events;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.neoforge.networking.NeoForgeHighlightNetworking;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.UUID;

@EventBusSubscriber(modid = Constants.ID)
public class PlayerDisconnect {
    private PlayerDisconnect() {}

    /** 鐜╁閫€鍑轰簨浠?*/
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            UUID playerId = player.getUUID();
            Utils.clearVeinMineState(playerId);
            NeoForgeHighlightNetworking.clearLastStates(playerId);
        }
    }
}

package top.diaoyugan.veinmine.fabric.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import top.diaoyugan.veinmine.events.BlockBreak;

public class PlayerBreakBlock {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(BlockBreak::onBlockBreak);
    }
}

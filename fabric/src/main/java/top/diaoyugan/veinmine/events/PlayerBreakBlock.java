package top.diaoyugan.veinmine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import top.diaoyugan.veinmine.VeinmineCore;

public class PlayerBreakBlock {
    public static void register() {
        // 注册方块破坏事件
        PlayerBlockBreakEvents.AFTER.register(VeinmineCore::onBlockBreak);
    }
}

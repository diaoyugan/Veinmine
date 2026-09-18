package top.diaoyugan.veinmine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class FabricPlayerBreakBlock {
    public static void register() {
        // 注册方块破坏事件
        PlayerBlockBreakEvents.AFTER.register(BlockBreak::onBlockBreak);
    }
}

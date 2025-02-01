package top.diaoyugan.vein_mine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;
import top.diaoyugan.vein_mine.Networking.keybindreciever.NetworkingKeybindPacket;



public class vein_mine implements ModInitializer {
    public static final String ID = "vein_mine";
    @Override
    public void onInitialize() {
        // 注册
        PlayerBreakBlock.register();
        new NetworkingKeybindPacket().onInitialize();
        new HighlightBlock().onInitialize();
        InvisibleBlock.initialize();
        ServerTickEvents.END_SERVER_TICK.register(HighlightBlock::tryRemoveGlowingBlock);
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            // 当玩家断开连接时清除该玩家的高亮方块
            HighlightBlock.tryRemoveGlowingBlocks(handler.getPlayer());
        });
    }
}

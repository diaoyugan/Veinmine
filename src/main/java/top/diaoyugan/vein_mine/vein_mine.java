package top.diaoyugan.vein_mine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;
import top.diaoyugan.vein_mine.Networking.keybindreciever.NetworkingKeybindPacket;

import static top.diaoyugan.vein_mine.Networking.HighlightBlock.tryRemoveGlowingBlock;


public class vein_mine implements ModInitializer {

    @Override
    public void onInitialize() {
        // 注册
        PlayerBreakBlock.register();
        new NetworkingKeybindPacket().onInitialize();
        new HighlightBlock().onInitialize();
        InvisibleBlock.initialize();
        ServerTickEvents.END_SERVER_TICK.register(HighlightBlock::tryRemoveGlowingBlock);
    }
}

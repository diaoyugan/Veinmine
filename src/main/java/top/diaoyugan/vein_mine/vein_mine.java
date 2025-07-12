package top.diaoyugan.vein_mine;

import net.fabricmc.api.ModInitializer;
import top.diaoyugan.vein_mine.networking.HighlightBlock;
import top.diaoyugan.vein_mine.networking.keybindreciever.NetworkingKeybindingPacket;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;


public class vein_mine implements ModInitializer {
    public static final String ID = "vein_mine";

    @Override
    public void onInitialize() {
        // 注册
        PlayerBreakBlock.register();
        new NetworkingKeybindingPacket().onInitialize();
        new HighlightBlock().onInitialize();
    }
}

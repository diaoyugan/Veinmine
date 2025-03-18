package top.diaoyugan.vein_mine;

import net.fabricmc.api.ModInitializer;
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
    }
}

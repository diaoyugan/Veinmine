package top.diaoyugan.vein_mine;


import net.fabricmc.api.ModInitializer;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;
import top.diaoyugan.vein_mine.keybindreciever.NetworkingKeybindPacket;


public class vein_mine implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerBreakBlock.register();
        new NetworkingKeybindPacket().onInitialize();
    }
}

package top.diaoyugan.vein_mine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import top.diaoyugan.vein_mine.Networking.HighlightBlock;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;
import top.diaoyugan.vein_mine.Networking.keybindreciever.NetworkingKeybindPacket;



public class vein_mine implements ModInitializer {
    public static final String ID = "vein_mine";
    public static Config config;

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
        config = Config.load();
        System.out.println("配置加载成功！当前设置：");
        System.out.println("搜索半径: " + config.searchRadius);
        System.out.println("BFS 限制: " + config.BFSLimit);
        System.out.println("BFS忽略的方块: " + config.ignoredBlocks);
        System.out.println("使用 BFS: " + config.useBFS);
        System.out.println("使用半径搜索: " + config.useRadiusSearch);
        System.out.println("达到BFS限制时使用半径搜索: " + config.useRadiusSearchWhenReachBFSLimit);
    }
}

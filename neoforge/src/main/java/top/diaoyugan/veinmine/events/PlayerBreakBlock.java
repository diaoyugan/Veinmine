
package top.diaoyugan.veinmine.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.VeinmineCore;

@EventBusSubscriber(modid = Constants.ID)
public class PlayerBreakBlock {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Level level = event.getPlayer().level();
        VeinmineCore.onBlockBreak(level, player , pos, state, null);
    }
}


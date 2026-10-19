
package top.diaoyugan.veinmine.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import top.diaoyugan.veinmine.Constants;
import top.diaoyugan.veinmine.utils.Utils;

@EventBusSubscriber(modid = Constants.ID)
public class PlayerBreakBlock {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) return;
        if (!(event.getPlayer() instanceof ServerPlayer player)
                || !(event.getLevel() instanceof ServerLevel level)) return;
        if (!Utils.getVeinMineSwitchState(player)) return;

        BlockPos pos = event.getPos();
        // Direct destroyBlock calls bypass the packet handler's spawn protection and world border check.
        if (!level.mayInteract(player, pos)) {
            event.setCanceled(true);
            return;
        }

        BlockState state = event.getState();
        BlockBreak.onBlockBreak(level, player, pos, state, null);
    }
}


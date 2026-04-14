package top.diaoyugan.vein_mine.networking;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.utils.Messages;
import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.List;

/**
 * Version-independent server-side handlers invoked by each version's
 * {@link ServerNetBridge} implementation after a packet arrives.
 */
public final class NetHandlers {
    private NetHandlers() {}

    public static void handleKeyPress(ServerPlayerEntity player) {
        boolean currentState = Utils.toggleVeinMineSwitchState(player);
        Text message = Text.translatable("vm.switch_state").append(": ")
                .append(Text.translatable(currentState ? "options.on" : "options.off")
                        .styled(style -> style.withFormatting(currentState ? Formatting.GREEN : Formatting.RED)));
        Messages.sendMessage(player, message, true);
        ServerNetBridge.INSTANCE.sendKeyResponse(player, currentState);
    }

    public static void handleHighlightRequest(ServerPlayerEntity player, BlockPos pos) {
        if (!Utils.getVeinMineSwitchState(player)) return;

        ServerWorld world = player.getServerWorld();
        BlockState state = world.getBlockState(pos);
        List<BlockPos> blocks = SmartVein.findBlocks(world, pos, Registries.BLOCK.getId(state.getBlock()));
        if (blocks == null || blocks.isEmpty()) return;

        ServerNetBridge.INSTANCE.sendHighlightResponse(player, blocks);
    }
}

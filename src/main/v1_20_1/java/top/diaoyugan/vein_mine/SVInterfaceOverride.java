package top.diaoyugan.vein_mine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import top.diaoyugan.vein_mine.utils.ServerVersionInterface;

import java.util.List;

public class SVInterfaceOverride implements ServerVersionInterface {

    @Override
    public void sendMessage(ServerPlayerEntity player, Text message, Boolean isOnActionbar) {
        if (player == null || message == null) return;
        player.sendMessage(message, isOnActionbar != null && isOnActionbar);
    }

    @Override
    public int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, PlayerEntity player, BlockState state) {
        if (blocksToBreak == null) return 0;
        return Math.max(0, blocksToBreak.size() - 1);
    }

    @Override
    public void damageToolForVein(PlayerEntity player, ItemStack tool, int amount, Hand hand) {
        if (tool == null || tool.isEmpty() || amount <= 0) return;
        final EquipmentSlot slot = (hand == Hand.OFF_HAND) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        tool.damage(amount, player, (LivingEntity p) -> p.sendEquipmentBreakStatus(slot));
    }
}

package top.diaoyugan.veinmine.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.diaoyugan.veinmine.config.ConfigItems;
import top.diaoyugan.veinmine.utils.Messages;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static top.diaoyugan.veinmine.utils.Utils.getVeinMineSwitchState;

public class BlockBreak {
    private static final Set<UUID> VEIN_MINING_PLAYERS = ConcurrentHashMap.newKeySet();

    public static void onBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity entity) {
        if (!(world instanceof ServerLevel serverWorld) || !(player instanceof ServerPlayer serverPlayer)) return;
        if (!getVeinMineSwitchState(player)) return;
        if (!VEIN_MINING_PLAYERS.add(player.getUUID())) return;

        try {
            List<BlockPos> blocks = SmartVein.findBlocks(world, pos, state);
            if (blocks == null || blocks.isEmpty()) return;
            if (!checkDurabilityAndWarn(player, pos, blocks)) return;

            breakBlocks(serverPlayer, pos, state, blocks);
            moveDropsToCenter(serverWorld, pos, computeAABB(blocks));
        } finally {
            VEIN_MINING_PLAYERS.remove(player.getUUID());
        }
    }

    private static boolean checkDurabilityAndWarn(Player player, BlockPos centerPos, List<BlockPos> blocks) {
        ConfigItems config = Utils.getConfig();
        if (!config.protectTools || player.hasInfiniteMaterials()) return true;

        int totalCost = Utils.calculateTotalDurabilityCost(blocks, centerPos, player);
        if (Utils.hasEnoughDurability(player, totalCost)) return true;

        if (player instanceof ServerPlayer serverPlayer) {
            Component msg = Component.translatable("vm.warn.breakthroughs")
                    .withStyle(style -> style.applyFormat(ChatFormatting.RED));
            Messages.sendMessage(serverPlayer, msg, false);
        }
        return false;
    }

    private static void breakBlocks(
            ServerPlayer player,
            BlockPos centerPos,
            BlockState originalState,
            List<BlockPos> blocks
    ) {
        for (BlockPos pos : blocks) {
            if (pos.equals(centerPos)) continue;

            BlockState targetState = player.level().getBlockState(pos);
            if (!SmartVein.matchesTarget(originalState, targetState)) continue;

            player.gameMode.destroyBlock(pos);
        }
    }

    private static void moveDropsToCenter(ServerLevel world, BlockPos centerPos, AABB area) {
        Vec3 center = Vec3.atCenterOf(centerPos);
        List<Entity> drops = world.getEntitiesOfClass(
                Entity.class,
                area.inflate(1),
                entity -> entity instanceof ItemEntity || entity instanceof ExperienceOrb
        );

        for (Entity drop : drops) {
            drop.setPos(center);
        }
    }

    public static AABB computeAABB(List<BlockPos> blocks) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (BlockPos pos : blocks) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }

        return new AABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
    }
}

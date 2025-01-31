package top.diaoyugan.vein_mine.Networking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.DisplayEntity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import top.diaoyugan.vein_mine.utils.Logger;

import top.diaoyugan.vein_mine.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HighlightBlock implements ModInitializer {
    public static final Identifier HIGHLIGHT_PACKET_ID = Networking.id("block_highlight");

    public record BlockHighlightPayload(BlockPos blockPos) implements CustomPayload {
        public static final Id<BlockHighlightPayload> ID = new CustomPayload.Id<>(HighlightBlock.HIGHLIGHT_PACKET_ID);
        public static final PacketCodec<PacketByteBuf, BlockHighlightPayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockHighlightPayload::blockPos, BlockHighlightPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    @Override
    public void onInitialize() {
        //PayloadTypeRegistry.playS2C().register(BlockHighlightPayload.ID, BlockHighlightPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(BlockHighlightPayload.ID, BlockHighlightPayload.CODEC);
        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, BlockHighlightPayload.ID, HighlightBlock::receive));
    }

    private static final Set<BlockPos> activeGlowingBlocks = new HashSet<>();
    private static void receive(BlockHighlightPayload payload, ServerPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        int radius = 1;  // 搜索范围

        ServerWorld world = context.player().getServerWorld();
        Block targetBlock = world.getBlockState(pos).getBlock();

        Set<BlockPos> newGlowingBlocks = new HashSet<>();

        if (Utils.getVeinMineSwitchState()) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos targetPos = pos.add(x, y, z);
                        if (world.getBlockState(targetPos).getBlock() == targetBlock) {
                            newGlowingBlocks.add(targetPos);
                            spawnGlowingBlock(world, targetPos);
                        }
                    }
                }
            }
        }

        // 移除不在新列表中的实体
        removeUnusedGlowingBlocks(world, newGlowingBlocks);

        // 更新存储的活跃实体
        activeGlowingBlocks.clear();
        activeGlowingBlocks.addAll(newGlowingBlocks);
    }


    public static void tryRemoveGlowingBlock(MinecraftServer server) {
        if (!Utils.getVeinMineSwitchState()) {
            for (ServerWorld world : server.getWorlds()) {
                for (BlockPos pos : activeGlowingBlocks) {
                    world.getEntitiesByType(EntityType.BLOCK_DISPLAY, entity ->
                            entity.getBlockPos().equals(pos)).forEach(entity -> {
                        entity.remove(Entity.RemovalReason.DISCARDED);
                        Logger.throwLog("info", "Removed entity at " + pos + " in world " + world.getRegistryKey().getValue());
                    });
                }
            }
            activeGlowingBlocks.clear(); // 清空已移除的实体位置
        }
    }




    public static void spawnGlowingBlock(ServerWorld world, BlockPos pos) {
        String entityName = "invisible_block_entity_" + pos.asLong();

        DisplayEntity existingEntity = world.getEntitiesByType(EntityType.BLOCK_DISPLAY, entity ->
                        entity.getCustomName() != null && entity.getCustomName().getString().equals(entityName))
                .stream().findFirst().orElse(null);

        if (existingEntity != null) {
            return;  // 该位置已有实体，直接返回
        }

        DisplayEntity displayEntity = EntityType.BLOCK_DISPLAY.create(world, SpawnReason.EVENT);
        if (displayEntity == null) return;

        NbtCompound nbt = new NbtCompound();
        NbtCompound blockStateTag = new NbtCompound();
        blockStateTag.putString("Name", "vein_mine:invisible_block");
        nbt.put("block_state", blockStateTag);

        displayEntity.readNbt(nbt);
        displayEntity.setPosition(Vec3d.of(pos));
        displayEntity.setCustomName(Text.of(entityName));
        displayEntity.setGlowing(true);

        world.spawnEntity(displayEntity);
        Logger.throwLog("info", "Created entity at " + pos);
    }

    private static void removeUnusedGlowingBlocks(ServerWorld world, Set<BlockPos> newGlowingBlocks) {
        for (DisplayEntity entity : world.getEntitiesByType(EntityType.BLOCK_DISPLAY, e -> e.getCustomName() != null)) {
            BlockPos entityPos = entity.getBlockPos();
            String entityName = "invisible_block_entity_" + entityPos.asLong();

            if (!newGlowingBlocks.contains(entityPos)) {
                entity.remove(Entity.RemovalReason.DISCARDED);
                Logger.throwLog("info", "Removed unused entity at " + entityPos);
            }
        }
    }

}

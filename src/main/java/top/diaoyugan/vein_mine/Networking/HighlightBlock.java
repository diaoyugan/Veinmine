package top.diaoyugan.vein_mine.Networking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.DisplayEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import net.minecraft.world.World;

import top.diaoyugan.vein_mine.utils.SmartVein;
import top.diaoyugan.vein_mine.utils.Utils;

import java.util.*;


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
        PayloadTypeRegistry.playC2S().register(BlockHighlightPayload.ID, BlockHighlightPayload.CODEC);
        ServerPlayConnectionEvents.INIT.register((handler, server) -> ServerPlayNetworking.registerReceiver(handler, BlockHighlightPayload.ID, HighlightBlock::receive));
    }

    private static final Map<UUID, Set<BlockPos>> playerGlowingBlocks = new HashMap<>();

    private static void receive(BlockHighlightPayload payload, ServerPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        PlayerEntity player = context.player();
        ServerWorld world = (ServerWorld) player.getWorld();
        BlockState state = world.getBlockState(pos);

        // 获取方块的命名空间 ID
        Identifier blockID = Registries.BLOCK.getId(state.getBlock());
        String BlockID = blockID.toString();
        Set<String> IGNORED_BLOCKS = Set.of("minecraft:air");

        Set<BlockPos> newGlowingBlocks = new HashSet<>();

        if (Utils.getVeinMineSwitchState(player)) {
            if (IGNORED_BLOCKS.contains(BlockID)) {
                tryRemoveGlowingBlock(world.getServer());
            } else {
                List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos);
                if(blocksToBreak != null) {
                    for (BlockPos targetPos : blocksToBreak) {
                        newGlowingBlocks.add(targetPos);
                        spawnGlowingBlock(world, targetPos, player);
                    }
                }
            }
        }

        // 移除不在新列表中的实体
        removeUnusedGlowingBlocks(world, newGlowingBlocks, player);

        // 更新存储的活跃实体
        Set<BlockPos> playerGlowingPos = playerGlowingBlocks.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        playerGlowingPos.clear();
        playerGlowingPos.addAll(newGlowingBlocks);
    }



    public static void tryRemoveGlowingBlock(MinecraftServer server) {
        // 创建一个 Map 来缓存所有世界
        Map<RegistryKey<World>, ServerWorld> worldsMap = new HashMap<>();
        server.getWorlds().forEach(svWorld -> worldsMap.put(svWorld.getRegistryKey(), svWorld));

        // 遍历所有玩家
        for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Set<BlockPos> glowingBlocks = playerGlowingBlocks.get(player.getUuid());
            if (glowingBlocks != null && !Utils.getVeinMineSwitchState(player)) {
                // 遍历玩家的高亮方块
                for (BlockPos pos : glowingBlocks) {
                    // 遍历所有世界并移除匹配的实体
                    worldsMap.values().forEach(svWorld -> svWorld.getEntitiesByType(EntityType.BLOCK_DISPLAY, entity -> entity.getBlockPos().equals(pos)).forEach(entity -> {
                        // 移除实体
                        entity.remove(Entity.RemovalReason.DISCARDED);
                        //Logger.throwLog("info", "Removed entity at " + entity.getBlockPos() + " in world " + svWorld.getRegistryKey().getValue());
                    }));
                }
                glowingBlocks.clear(); // 清空该玩家已移除的实体位置
            }
        }
    }

    // 清理断开连接玩家的高亮方块
    public static void tryRemoveGlowingBlocks(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            // 创建一个 Map 来存储所有世界
            Map<RegistryKey<World>, ServerWorld> worldsMap = new HashMap<>();
            server.getWorlds().forEach(world -> worldsMap.put(world.getRegistryKey(), world));

            // 获取玩家的高亮方块
            Set<BlockPos> glowingBlocks = playerGlowingBlocks.get(player.getUuid());
            if (glowingBlocks != null) {
                // 遍历所有世界
                worldsMap.values().forEach(world -> {
                    // 遍历玩家的所有高亮方块
                    for (BlockPos pos : glowingBlocks) {
                        // 查找并移除在该位置的 BLOCK_DISPLAY 实体
                        world.getEntitiesByType(EntityType.BLOCK_DISPLAY, entity -> entity.getBlockPos().equals(pos)).forEach(entity -> {
                            entity.remove(Entity.RemovalReason.DISCARDED);
                            //Logger.throwLog("info", "Removed entity at " + pos + " in world " + world.getRegistryKey().getValue());
                        });
                    }
                });
                glowingBlocks.clear(); // 清空该玩家已移除的实体位置
            }
        }
    }



    public static void spawnGlowingBlock(ServerWorld world, BlockPos pos, PlayerEntity player) {
        String entityName = "invisible_block_entity_" + pos.asLong();

        // 获取玩家的UUID并更新其高亮方块位置集合
        UUID playerId = player.getUuid();
        Set<BlockPos> playerGlowingPos = playerGlowingBlocks.computeIfAbsent(playerId, k -> new HashSet<>());
        playerGlowingPos.add(pos);

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
        //Logger.throwLog("info", "Created entity at " + pos);
    }



    private static void removeUnusedGlowingBlocks(ServerWorld world, Set<BlockPos> newGlowingBlocks, PlayerEntity player) {
        UUID playerId = player.getUuid();
        Set<BlockPos> playerGlowingPos = playerGlowingBlocks.get(playerId);

        if (playerGlowingPos != null) {
            for (DisplayEntity entity : world.getEntitiesByType(EntityType.BLOCK_DISPLAY, e -> Objects.requireNonNull(e.getCustomName()).getString().contains("invisible_block_entity_"))) {
                BlockPos entityPos = entity.getBlockPos();

                if (!newGlowingBlocks.contains(entityPos)) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                    //Logger.throwLog("info", "Removed unused entity at " + entityPos + " for player " + player.getName());
                }
            }
        }
    }



}

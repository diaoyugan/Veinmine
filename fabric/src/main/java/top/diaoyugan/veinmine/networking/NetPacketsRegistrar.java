package top.diaoyugan.veinmine.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.networking.keypacket.KeyResponsePacket;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NetPacketsRegistrar {
    private NetPacketsRegistrar() {}

    public static void init() {
        // Keybinding packets
        PayloadTypeRegistry.serverboundPlay().register(KeyPressPacket.ID, KeyPressPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(KeyResponsePacket.ID, KeyResponsePacket.CODEC);

        // Block highlight packets
        PayloadTypeRegistry.serverboundPlay().register(BlockHighlightRequest.ID, BlockHighlightRequest.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(BlockHighlightResponse.ID, BlockHighlightResponse.CODEC);

        // Register C2S receiver for BlockHighlightRequest when a player connection initializes.
        ServerPlayConnectionEvents.INIT.register((handler, server) ->
                ServerPlayNetworking.registerReceiver(handler, BlockHighlightRequest.ID, NetPacketsRegistrar::handleBlockHighlightRequest)
        );
    }

    private static void handleBlockHighlightRequest(BlockHighlightRequest payload, ServerPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        ServerPlayer player = context.player();
        ServerLevel world = (ServerLevel) player.level();

        // 获取方块的命名空间 ID
        Set<BlockPos> newGlowingBlocks = new HashSet<>();

        if (Utils.getVeinMineSwitchState(player)) {
            List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos);
            if (blocksToBreak != null) {


                // 收集所有需要发送的方块
                newGlowingBlocks.addAll(blocksToBreak);

                ServerPlayNetworking.send(player, new BlockHighlightResponse(new ArrayList<>(newGlowingBlocks)));

            }
        }
    }
}
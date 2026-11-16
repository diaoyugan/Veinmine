package top.diaoyugan.veinmine.networking;

import io.netty.handler.codec.DecoderException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import top.diaoyugan.veinmine.config.MiningRules;

import java.util.HashSet;
import java.util.Set;

public record MiningPreferencesPacket(MiningRules rules) implements CustomPacketPayload {
    public static final Type<MiningPreferencesPacket> ID = new Type<>(Networking.id("mining_preferences"));
    private static final int MAX_IDS = 4096;
    private static final int MAX_ID_LENGTH = 256;

    public static final StreamCodec<FriendlyByteBuf, MiningPreferencesPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                MiningRules rules = packet.rules();
                buf.writeBoolean(rules.protectTools());
                buf.writeBoolean(rules.protectDefaultTools());
                buf.writeVarInt(rules.durabilityThreshold());
                writeIds(buf, rules.protectedTools());
                writeIds(buf, rules.ignoredBlocks());
            },
            buf -> new MiningPreferencesPacket(new MiningRules(
                    buf.readBoolean(), buf.readBoolean(), Math.max(0, buf.readVarInt()),
                    readIds(buf), readIds(buf)))
    );

    private static void writeIds(FriendlyByteBuf buf, Set<String> ids) {
        var validIds = ids.stream().filter(MiningPreferencesPacket::isValidId).sorted().limit(MAX_IDS).toList();
        buf.writeVarInt(validIds.size());
        for (String id : validIds) buf.writeUtf(id, MAX_ID_LENGTH);
    }

    private static Set<String> readIds(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        if (size < 0 || size > MAX_IDS) throw new DecoderException("Invalid mining preference list size: " + size);
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < size; i++) {
            String id = buf.readUtf(MAX_ID_LENGTH);
            if (isValidId(id)) ids.add(id);
        }
        return ids;
    }

    private static boolean isValidId(String id) {
        if (id.length() > MAX_ID_LENGTH) return false;
        Identifier parsed = Identifier.tryParse(id);
        return parsed != null && !parsed.getPath().isEmpty() && parsed.toString().equals(id);
    }

    @Override
    public Type<MiningPreferencesPacket> type() {
        return ID;
    }
}

package top.diaoyugan.veinmine.networking.keypacket;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.diaoyugan.veinmine.Constants;


@EventBusSubscriber(modid = Constants.ID, value = Dist.DEDICATED_SERVER)
public final class KeyPacketImplements {

    private KeyPacketImplements() {}


}

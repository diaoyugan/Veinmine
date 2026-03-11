package top.diaoyugan.veinmine.networking.keypacket;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import top.diaoyugan.veinmine.Constants;


@EventBusSubscriber(modid = Constants.ID, value = Dist.DEDICATED_SERVER)
public final class KeyPacketImplements {

    private KeyPacketImplements() {}


}

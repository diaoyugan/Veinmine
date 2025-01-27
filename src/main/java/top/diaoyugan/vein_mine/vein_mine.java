package top.diaoyugan.vein_mine;


import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.diaoyugan.vein_mine.events.PlayerBreakBlock;


public class vein_mine implements ModInitializer {
    public static final String ID = "vein-mine";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static Identifier id(String name) {
        return Identifier.of(ID, name);
    }

    private vein_mine() {
    }

    @Override
    public void onInitialize() {
        PlayerBreakBlock.register();
    }
}

package top.diaoyugan.vein_mine.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import top.diaoyugan.vein_mine.config.Config;
import top.diaoyugan.vein_mine.config.ConfigItems;

public class ConfigOverride {
    public static void createAdvanceConfig(ConfigBuilder cb, ConfigItems ci) {
        Config config = Config.getInstance();
        ConfigItems configItems = config.getConfigItems();
        ConfigCategory finalConfig = cb.getOrCreateCategory(Text.translatable("vm.config.screen.final_resort").styled(style -> style.withColor(Formatting.RED)));
        ConfigEntryBuilder entryBuilder = cb.entryBuilder();

        finalConfig.addEntry(entryBuilder.startBooleanToggle(Text.translatable("vm.config.useIntrusiveCode").styled(style -> style.withColor(Formatting.RED)), ci.useIntrusiveCode)
                .setTooltip(Text.translatable("vm.config.useIntrusiveCode.tooltip").styled(style -> style.withColor(Formatting.RED)))
                .setDefaultValue(true)
                .setSaveConsumer((Boolean b) -> configItems.useIntrusiveCode = b)
                .build());
    }
}

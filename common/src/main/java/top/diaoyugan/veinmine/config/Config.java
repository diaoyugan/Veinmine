package top.diaoyugan.veinmine.config;

// No NULL VALUE allowed!!! I'm fed up with NPE

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import top.diaoyugan.veinmine.utils.logging.Logger;
import top.diaoyugan.veinmine.utils.logging.LoggerLevels;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class Config {
    private final Path configFilePath = Paths.get("config/veinmine.json");
    private final Gson jsonProcessor = new GsonBuilder().setPrettyPrinting().create();
    private final AtomicLong revision = new AtomicLong();
    private volatile ConfigItems configItems = new ConfigItems();
    private volatile MiningSettings miningSettings = MiningSettings.from(configItems);

    private static class Holder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return Holder.INSTANCE;
    }

    private Config() {
        try {
            // Make sure the config directory exists
            Files.createDirectories(configFilePath.getParent());
            // Create and save default config if not exists
            if (!Files.exists(configFilePath)) {
                save();
            }
            load();
        } catch (Exception e) {
            Logger.throwLog(LoggerLevels.ERROR, "Failed to initialize the configuration", e);
        }
    }

    public void reset(){
        configItems = new ConfigItems();
        save();
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(configFilePath)) {
            jsonProcessor.toJson(configItems, writer);
        } catch (Exception e) {
            Logger.throwLog(LoggerLevels.ERROR, "Failed to save the configuration", e);
        } finally {
            publishMiningRules();
        }
    }

//    public void apply() {
//        // 重新加载配置
//        load();
//        Logger.throwLog(LoggerLevels.INFO, "Reloaded and applied config!");
//    }

    public void load() {
        try (Reader reader = Files.newBufferedReader(configFilePath)) {
            ConfigItems loadedConfig = jsonProcessor.fromJson(reader, ConfigItems.class);
            if (loadedConfig != null) {
                boolean migrated = ConfigMigration.migrate(loadedConfig);
                configItems = loadedConfig;
                IntrusiveConfig.load(configItems);
                if (migrated) {
                    save();
                }
            } else {
                // Handle null case
                configItems = new ConfigItems();
                save();
            }
        } catch (JsonSyntaxException e) {
            Logger.throwLog(LoggerLevels.ERROR, "The configuration file is malformed, reset to the default configuration",
                    e.getMessage());
            reset();
        } catch (IOException e) {
            Logger.throwLog(LoggerLevels.ERROR, "Failed to read the configuration file, using the default configuration",e);
            configItems = new ConfigItems();
        } finally {
            publishMiningRules();
        }
    }

    public ConfigItems getConfigItems() {
        return configItems;
    }

    public long revision() {
        return revision.get();
    }

    public MiningRules miningRules() {
        return miningSettings.rules();
    }

    MiningSettings miningSettings() {
        return miningSettings;
    }

    private void publishMiningRules() {
        miningSettings = MiningSettings.from(configItems);
        revision.incrementAndGet();
    }

    record MiningSettings(ConfigItems.ServerRuleMode mode, MiningRules rules) {
        private static MiningSettings from(ConfigItems config) {
            return new MiningSettings(config.serverRuleMode, MiningRules.from(config));
        }
    }
}

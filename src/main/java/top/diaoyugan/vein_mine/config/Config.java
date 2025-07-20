package top.diaoyugan.vein_mine.config;

// No NULL VALUE allowed!!! I'm fed up with NPE

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import top.diaoyugan.vein_mine.utils.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private final Path configFilePath = Paths.get("config/vein_mine.json");
    private final Gson jsonProcessor = new GsonBuilder().setPrettyPrinting().create();
    private ConfigItems configItems = new ConfigItems();

    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
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
            Logger.throwLog("error", "Failed to initialize the configuration", e);
        }
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(configFilePath)) {
            jsonProcessor.toJson(configItems, writer);
        } catch (Exception e) {
            Logger.throwLog("error", "Failed to save the configuration", e);
        }
    }

//    public void apply() {
//        // 重新加载配置
//        load();
//        Logger.throwLog("info", "Reloaded and applied config!");
//    }

    public void load() {
        try (Reader reader = Files.newBufferedReader(configFilePath)) {
            ConfigItems loadedConfig = jsonProcessor.fromJson(reader, ConfigItems.class);
            if (loadedConfig != null) {
                configItems = loadedConfig;
                IntrusiveConfig.load(configItems);
            } else {
                // Handle null case
                configItems = new ConfigItems();
                save();
            }
        } catch (JsonSyntaxException e) {
            Logger.throwLog("error", "The configuration file is malformed, reset to the default configuration");
            configItems = new ConfigItems();
            save();
        } catch (IOException e) {
            Logger.throwLog("error", "Failed to read the configuration file, using the default configuration");
            configItems = new ConfigItems();
        }
    }

    public ConfigItems getConfigItems() {
        return configItems;
    }


}
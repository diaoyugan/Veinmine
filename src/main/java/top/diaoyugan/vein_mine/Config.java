package top.diaoyugan.vein_mine;

// No NULL VALUE allowed!!! I'm fed up with NPE

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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
    public Config() {
        try {
            // Make sure the config directory exists
            Files.createDirectories(configFilePath.getParent());
            // Create and save default config if not exists
            if (!Files.exists(configFilePath)) {
                save();
            }
            load();
        } catch (Exception e) {
            throw new RuntimeException("初始化配置失败", e);
        }
    }
    public void save() {
        try (Writer writer = Files.newBufferedWriter(configFilePath)) {
            jsonProcessor.toJson(configItems, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void apply(){ // Event

    }
    public void updateConfigVersion(){
        // Not yet implemented
    }
    public void load() {
        try (Reader reader = Files.newBufferedReader(configFilePath)) {
            ConfigItems loadedConfig = jsonProcessor.fromJson(reader, ConfigItems.class);
            if (loadedConfig != null) {
                configItems = loadedConfig;
            } else {
                // Handle null case
                configItems = new ConfigItems();
                save();
            }
        } catch (JsonSyntaxException e) {
            System.err.println("配置文件格式错误，重置为默认配置");
            configItems = new ConfigItems();
            save();
        } catch (IOException e) {
            System.err.println("读取配置文件失败，使用默认配置");
            configItems = new ConfigItems();
        }
    }
    public ConfigItems getConfigItems(){
        return configItems;
    }
}

/* Logic

 */
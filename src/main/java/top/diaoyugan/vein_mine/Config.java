package top.diaoyugan.vein_mine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import top.diaoyugan.vein_mine.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/vein_mine.json");

    // 默认配置项
    public int searchRadius = 1;
    public int BFSLimit = 50;
    public Set<String> ignoredBlocks = new HashSet<>(Set.of("minecraft:air"));
    public boolean useBFS = true;
    public boolean useRadiusSearch = true;
    public boolean useRadiusSearchWhenReachBFSLimit = true;

    //读取配置文件，如果文件不存在或损坏，则使用默认值
    public static Config load() {
        // 确保 config 目录存在
        if (!CONFIG_FILE.getParentFile().exists()) {
            boolean created = CONFIG_FILE.getParentFile().mkdirs();
            if (!created) {
                Logger.throwLog("error", "Failed to create config directory.");
            }
        }

        // 如果文件不存在，创建并保存默认配置
        if (!CONFIG_FILE.exists()) {
            Config defaultConfig = new Config();
            defaultConfig.save();
            return defaultConfig;
        }

        // 读取 JSON 配置
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Config loadedConfig = GSON.fromJson(reader, Config.class);

            // 确保重要字段不为 null，避免手动编辑 JSON 出错
            if (loadedConfig.ignoredBlocks == null) {
                loadedConfig.ignoredBlocks = new HashSet<>(Set.of("minecraft:air"));
            }
            return loadedConfig;
        } catch (com.google.gson.JsonSyntaxException e) {
            Logger.throwLog("error", "Config file is corrupted. Resetting to default values.\n" + e);
            try {
                if (!CONFIG_FILE.delete()) {
                    Logger.throwLog("error", "Failed to delete corrupted config file.");
                }
            } catch (SecurityException se) {
                Logger.throwLog("error", "Security exception occurred while trying to delete the config file: \n" + se);
            }
        } catch (IOException e) {
            Logger.throwLog("error", "Failed to read config file. Using default values.\n" + e);
        }

        return new Config(); // 读取失败，返回默认配置
    }


     //保存当前配置到 JSON 文件
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            Logger.throwLog("error", "Failed to save config file.\n" + e);
        }
    }
}

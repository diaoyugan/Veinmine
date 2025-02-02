package top.diaoyugan.vein_mine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import top.diaoyugan.vein_mine.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
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

    // 当前配置实例
    private static Config currentConfig;

    // 初始化配置（用于初次启动）
    public static void initialize() {
        currentConfig = load();  // 加载配置
        System.out.println("Configuration loaded successfully on initial start.");
        printConfig();  // 打印当前配置
    }

    // 获取当前的配置实例
    public static Config getCurrentConfig() {
        return currentConfig;
    }

    // 热重载配置（可调用此方法进行配置刷新）
    public static void reload() {
        currentConfig = load();  // 重新加载配置
        System.out.println("Configuration reloaded successfully.");
        printConfig();  // 打印新的配置
    }

    // 打印当前配置（可以在初始化或重载时调用）
    private static void printConfig() {
        System.out.println("Search Radius: " + currentConfig.searchRadius);
        System.out.println("BFS Limit: " + currentConfig.BFSLimit);
        System.out.println("Ignored Blocks: " + currentConfig.ignoredBlocks);
        System.out.println("Use BFS: " + currentConfig.useBFS);
        System.out.println("Use Radius Search: " + currentConfig.useRadiusSearch);
        System.out.println("Use Radius Search When Reach BFS Limit: " + currentConfig.useRadiusSearchWhenReachBFSLimit);
    }

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

            // 确保 ignoredBlocks 不为 null 并且避免重复添加 "minecraft:air"
            if (loadedConfig.ignoredBlocks == null) {
                loadedConfig.ignoredBlocks = new HashSet<>(Set.of("minecraft:air"));
            } else {
                // 如果 config 中没有 "minecraft:air"，手动添加
                loadedConfig.ignoredBlocks.add("minecraft:air");
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

    public void modify(String fieldName, Object newValue, String action) {
        try {
            // 通过反射获取配置项的字段
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            // 判断字段类型是否是 Set<String>，如果是就处理集合的操作
            if (field.getType().equals(Set.class)) {
                // 如果是 ignoredBlocks 并且 newValue 是 String 类型
                if (fieldName.equals("ignoredBlocks") && newValue instanceof String) {
                    Set<?> ignoredBlocks = (Set<?>) field.get(this); // 获取当前 ignoredBlocks 的值

                    // 确保 ignoredBlocks 是 Set<String> 类型
                    if (ignoredBlocks instanceof Set) {
                        // 安全地转换为 Set<String>
                        @SuppressWarnings("unchecked")
                        Set<String> castedIgnoredBlocks = (Set<String>) ignoredBlocks;

                        // 根据操作类型执行新增或移除
                        if ("add".equals(action)) {
                            castedIgnoredBlocks.add((String) newValue);
                            Logger.throwLog("info", "Added block to ignoredBlocks: " + newValue);
                        } else if ("remove".equals(action)) {
                            castedIgnoredBlocks.remove(newValue);
                            Logger.throwLog("info", "Removed block from ignoredBlocks: " + newValue);
                        } else {
                            Logger.throwLog("error", "Invalid action: " + action);
                        }
                    }
                } else {
                    // 对其他 Set 类型的字段直接修改
                    field.set(this, newValue);
                }
            } else {
                // 其他普通类型的字段，直接修改
                field.set(this, newValue);
            }

            // 保存修改后的配置并尝试热重载
            save();

            Logger.throwLog("info", "Configuration updated: " + fieldName + " = " + newValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Logger.throwLog("error", "Failed to modify config field: " + fieldName + "\n" + e);
        }
    }

    // 保存当前配置到 JSON 文件
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
            // 保存完成后，尝试热重载配置
            Logger.throwLog("info", "Configuration saved successfully.");

            // 重新加载配置，进行热重载
            Config.reload();
        } catch (IOException e) {
            Logger.throwLog("error", "Failed to save config file.\n" + e);
        }
    }

}

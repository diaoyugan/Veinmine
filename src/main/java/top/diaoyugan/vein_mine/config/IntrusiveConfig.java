package top.diaoyugan.vein_mine.config;

public class IntrusiveConfig {
    private static boolean useIntrusiveCode;

    public static void load(ConfigItems config) {
        useIntrusiveCode = config.useIntrusiveCode;
    }

    public static boolean isEnabled() {
        return useIntrusiveCode;
    }
}


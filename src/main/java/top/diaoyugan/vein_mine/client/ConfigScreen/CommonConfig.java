package top.diaoyugan.vein_mine.client.configScreen;

public final class CommonConfig {
    private static IVersionConfigProvider provider = new VersionDefault();
    public static void setVersionProvider(IVersionConfigProvider p) { provider = p; }
    public static IVersionConfigProvider getVersionProvider() { return provider; }
}

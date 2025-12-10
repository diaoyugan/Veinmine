package top.diaoyugan.vein_mine.client.configScreen;

public class Version_1_21_11 implements IVersionConfigProvider {
    @Override
    public boolean allowOption(String optionId) {
        if ("new_placeholder_option".equals(optionId)) return true;
        return true;
    }
}
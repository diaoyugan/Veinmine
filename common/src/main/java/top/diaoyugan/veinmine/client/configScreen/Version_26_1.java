package top.diaoyugan.veinmine.client.configScreen;

public class Version_26_1 implements IVersionConfigProvider {
    @Override
    public boolean allowOption(String optionId) {
        if ("new_placeholder_option".equals(optionId)) return true;
        return true;
    }
}
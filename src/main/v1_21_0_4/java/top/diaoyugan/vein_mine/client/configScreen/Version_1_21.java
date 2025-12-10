package top.diaoyugan.vein_mine.client.configScreen;

public class Version_1_21 implements IVersionConfigProvider {
    @Override
    public boolean allowOption(String optionId) {
        // 禁用 id 为 "use_intrusive_code" 的项
        if ("use_intrusive_code".equals(optionId)) return false;
        return true;
    }
}


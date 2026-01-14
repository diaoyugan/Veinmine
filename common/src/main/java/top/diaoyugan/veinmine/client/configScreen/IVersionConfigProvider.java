package top.diaoyugan.veinmine.client.configScreen;


public interface IVersionConfigProvider {
    /** 根据配置项 id 决定该项是否被允许显示/添加（build 时由版本 jar 提供实现） */
    default boolean allowOption(String optionId) {
        return true;
    }
}




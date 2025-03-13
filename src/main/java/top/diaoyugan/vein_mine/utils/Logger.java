package top.diaoyugan.vein_mine.utils;

import org.slf4j.LoggerFactory;

import static top.diaoyugan.vein_mine.vein_mine.ID;


public class Logger {

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ID);

    // throwLog 方法处理不同类型的日志
    public static void throwLog(String type, String message, Object... params) {
        switch (type) {
            case "warn" -> LOGGER.warn(message, params);  // 直接用 SLF4J 的参数化日志
            case "error" -> LOGGER.error(message, params);
            case "debug" -> LOGGER.debug(message, params);
            default -> LOGGER.info(message, params);
        }
    }

    
    // 简单日志方法，不需要异常参数
    public static void throwLog(String type, String message) {
        switch (type) {
            case "warn" -> LOGGER.warn(message);
            case "error" -> LOGGER.error(message);
            case "debug" -> LOGGER.debug(message);
            default -> LOGGER.info(message);
        }
    }
}

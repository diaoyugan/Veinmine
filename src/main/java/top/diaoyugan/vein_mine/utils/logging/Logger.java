package top.diaoyugan.vein_mine.utils.logging;

import org.slf4j.LoggerFactory;

import static top.diaoyugan.vein_mine.vein_mine.ID;


/**
 * 日志工具类，用于记录不同等级的日志信息。
 */
public class Logger {

    /** SLF4J 日志实例，使用项目 ID 作为 Logger 名称 */
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ID);

    /**
     * 根据日志等级记录日志，支持参数化信息。
     *
     * @param type    日志等级（LoggerLevels.WARN、ERROR、DEBUG、INFO 等）
     * @param message 日志内容
     * @param params  日志内容参数，可选
     */
    public static void throwLog(LoggerLevels type, String message, Object... params) {
        switch (type) {
            case WARN -> LOGGER.warn(message, params);  // 使用 SLF4J 参数化日志
            case ERROR -> LOGGER.error(message, params);
            case DEBUG -> LOGGER.debug(message, params);
            default -> LOGGER.info(message, params);
        }
    }

    /**
     * 简单日志记录方法，不带参数。
     *
     * @param type    日志等级
     * @param message 日志内容
     */
    public static void throwLog(LoggerLevels type, String message) {
        switch (type) {
            case WARN -> LOGGER.warn(message);
            case ERROR -> LOGGER.error(message);
            case DEBUG -> LOGGER.debug(message);
            default -> LOGGER.info(message);
        }
    }
}
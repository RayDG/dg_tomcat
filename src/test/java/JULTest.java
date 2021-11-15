import org.junit.jupiter.api.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @Author: DG
 * @Date: 2021/11/5 22:41
 * @Description:
 */
public class JULTest {

    @Test
    public void quickStart() {
        // 1.获取日志记录器对象
        Logger logger = Logger.getLogger(JULTest.class.getName());
        // 2.日志记录输出
        logger.info("Hello JUL!");

        // 通用方法进行日志记录
        logger.log(Level.INFO, "info msg");

        // 通过占位符输出变量值
        String name = "DG";
        int age = 22;
        logger.log(Level.INFO, "用户信息 {0},{1}", new Object[]{name, age});
    }

    @Test
    public void logLevelTest() {
        // 1.获取日志记录器对象
        Logger logger = Logger.getLogger(JULTest.class.getName());

        // 2.日志记录输出
        logger.severe("severe");
        logger.warning("warning");
        logger.info("info");    // 默认日志级别 info
        logger.config("config");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }

    // 自定义日志级别
    @Test
    public void testLogConfig() {
        // 1.获取日志记录器对象
        Logger logger = Logger.getLogger(JULTest.class.getName());

        // 关闭系统默认配置
        logger.setUseParentHandlers(false);

        // 自定义配置日志级别
        // 创建 ConsoleHandler
        ConsoleHandler consoleHandler = new ConsoleHandler();

        // 创建简单格式转换对象
        SimpleFormatter simpleFormatter = new SimpleFormatter();

        // 关联
        consoleHandler.setFormatter(simpleFormatter);
        logger.addHandler(consoleHandler);

        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);

        // 2.日志记录输出
        logger.severe("severe");
        logger.warning("warning");
        logger.info("info");    // 默认日志级别 info
        logger.config("config");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");

    }
}

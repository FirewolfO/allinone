package com.firewolf.pattern.factory_method.simple;

/**
 * 描述：工厂类抽象
 * Author：liuxing
 * Date：2020/5/14
 */
public abstract class LoggerFactory {

    /**
     * 工厂方法最好修饰成protected，对客户端进行隐藏
     * @return
     */
    protected abstract Logger createLogger();

    public void writeLog(String content){
        Logger logger = this.createLogger();
        logger.writeLog(content);
    }
}

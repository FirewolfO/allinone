package com.megvii.ebg.pangu.ms.base.provider.setup;

import org.testcontainers.containers.GenericContainer;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/29
 */
public class Redis extends TestContainer {

    private GenericContainer redis = new GenericContainer<>("redis:5.0.5").withExposedPorts(6379)
            .withCommand("--requirepass 123456");

    public Redis() {
    }


    public Redis(TestContainer testContainer) {
        super(testContainer);
    }

    @Override
    public void startContainer() {
        log.info("starting redis container ... ");
        redis.start();

        Integer mappedPort = redis.getMappedPort(6379);
        String ipAddress = redis.getContainerIpAddress();
        System.setProperty("redis.host", ipAddress);
        System.setProperty("redis.port", mappedPort.intValue() + "");
        System.setProperty("redis.password", "123456");

        log.info("test redis url = {}", ipAddress);
        log.info("test redis port = {}", mappedPort);
        log.info("test redis password = 123456");

        log.info("redis container started !!!");
    }

    @Override
    public void stopContainer() {
        redis.stop();
    }
}

package com.megvii.ebg.pangu.ms.base.provider.setup.rocket;

import com.megvii.ebg.pangu.ms.base.provider.setup.TestContainer;
import org.testcontainers.containers.Network;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/29
 */
public class RocketMQ extends TestContainer {

    static final int MQ_SERVER_PORT = 9876;
    static final String NAMESRV = "namesrv";

    private final Network NETWORK = Network.newNetwork();

    private final RocketMqNameServerContainer rocketMqNameSrv = new RocketMqNameServerContainer()
            .withNetwork(NETWORK)
            .withNetworkAliases(NAMESRV)
            .withFixedExposedPort(32951, MQ_SERVER_PORT);

    private final RocketMqBrokerContainer rocketMqBroker = new RocketMqBrokerContainer()
            .withNetwork(NETWORK)
            .withEnv("NAMESRV_ADDR", "namesrv:" + MQ_SERVER_PORT)
            .withFixedExposedPort(10909, 10909)
            .withFixedExposedPort(10911, 10911);

    public RocketMQ() {
    }

    public RocketMQ(TestContainer testContainer) {
        super(testContainer);
    }

    @Override
    public void startContainer() {

        // 启动nameServer
        rocketMqNameSrv.start();

        Integer mappedPort = rocketMqNameSrv.getMappedPort(MQ_SERVER_PORT);
        String ipAddress = rocketMqNameSrv.getContainerIpAddress();

        // 启动broker
        rocketMqBroker.start();


        log.info("test rocket mq url= {}", ipAddress);
        log.info("test rocket port= {} ", mappedPort);
        System.setProperty("mq.rocket.nameSrvAddr", ipAddress + ":" + mappedPort);

    }

    @Override
    public void stopContainer() {
        rocketMqBroker.stop();
        rocketMqNameSrv.stop();
    }
}

package com.megvii.ebg.pangu.ms.base.provider.setup.rocket;

import org.testcontainers.containers.FixedHostPortGenericContainer;

import static org.testcontainers.containers.BindMode.READ_ONLY;

public class RocketMqBrokerContainer extends FixedHostPortGenericContainer<RocketMqBrokerContainer> {

  public RocketMqBrokerContainer() {
    super("seanyinx/rocketmq-broker:4.3.0");
  }

  @Override
  protected void configure() {
    super.configure();

    withClasspathResourceMapping("docker/rocketmq/broker.properties", "/opt/rocketmq-4.3.0/conf/broker.properties", READ_ONLY);
  }
}
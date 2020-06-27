package com.megvii.ebg.pangu.ms.base.provider.setup.rocket;

import org.testcontainers.containers.FixedHostPortGenericContainer;

public class RocketMqNameServerContainer extends FixedHostPortGenericContainer<RocketMqNameServerContainer> {

  public RocketMqNameServerContainer() {
    super("seanyinx/rocketmq-namesrv:4.3.0");
  }
}
package com.megvii.ebg.pangu.ms.base.provider.setup;

import com.megvii.ebg.pangu.ms.base.provider.setup.rocket.RocketMQ;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/21
 */
public class TestContainerHelper {

    public static TestContainer getAllContainters() {
        return new Redis(new ES(new MariaDB(new RocketMQ())));
    }
}

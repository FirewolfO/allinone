package com.megvii.ebg.pangu.ms.base.provider.setup;

import com.firewolf.learn.test.Logger;

/**
 * 描述：容器抽象
 * Author：liuxing
 * Date：2020/5/29
 */
public abstract class TestContainer {

    protected static Logger log = new Logger();

    private TestContainer testContainer;

    public abstract void startContainer();

    public abstract void stopContainer();

    public void start() {
        if (testContainer != null) {
            testContainer.start();
        }
        this.startContainer();
    }

    public void stop() {
        if (testContainer != null) {
            testContainer.stop();
        }
        this.stopContainer();
    }

    public TestContainer() {
    }

    public TestContainer(TestContainer testContainer) {
        this.testContainer = testContainer;
    }
}

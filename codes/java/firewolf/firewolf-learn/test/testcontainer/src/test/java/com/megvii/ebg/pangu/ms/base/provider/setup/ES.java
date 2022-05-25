package com.megvii.ebg.pangu.ms.base.provider.setup;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/29
 */
public class ES extends TestContainer {

    private ElasticsearchContainer es = new ElasticsearchContainer("elasticsearch:7.6.2");

    public ES() {
    }

    public ES(TestContainer testContainer) {
        super(testContainer);
    }

    @Override
    public void startContainer() {
        log.info("starting es container ......... ");
        es.start();
        String ipAndPort = es.getHttpHostAddress();
        log.info("test es url ={} ", es.getHttpHostAddress());
        System.setProperty("elasticsearch.commaNodes", ipAndPort);
        log.info("es container started !!! ");
    }

    @Override
    public void stopContainer() {
        es.stop();
    }
}

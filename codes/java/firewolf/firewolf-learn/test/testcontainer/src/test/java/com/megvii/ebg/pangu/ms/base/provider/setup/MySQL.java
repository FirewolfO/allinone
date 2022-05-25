package com.megvii.ebg.pangu.ms.base.provider.setup;

import org.testcontainers.containers.MySQLContainer;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/29
 */
public class MySQL extends TestContainer {

    private MySQLContainer mysql = (MySQLContainer) new MySQLContainer("mysql:5.7") // 指定mysql版本
            .withInitScript("sql/base_test.sql")//指定初始化SQL文件
            .withCommand("--character-set-server=utf8 --collation-server=utf8_unicode_ci")//防止乱码，--skip-grant-tables
            .withExposedPorts(3306);

    public MySQL() {
    }

    public MySQL(TestContainer testContainer) {
        super(testContainer);
    }

    @Override
    public void startContainer() {
        log.info("starting mysql container ... ");
        mysql.start();
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.driver-class-name", mysql.getDriverClassName());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());

        log.info("test mysql url = {}", mysql.getJdbcUrl());
        log.info("test mysql username = {}", mysql.getUsername());
        log.info("test mysql password = {}", mysql.getPassword());
        log.info("test mysql driver = {}", mysql.getDriverClassName());

        log.info("mysql container started !!! ");
    }

    @Override
    public void stopContainer() {
        mysql.stop();
    }
}

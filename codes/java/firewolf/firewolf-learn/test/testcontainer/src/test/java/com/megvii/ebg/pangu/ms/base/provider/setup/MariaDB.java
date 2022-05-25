package com.megvii.ebg.pangu.ms.base.provider.setup;

import org.testcontainers.containers.MariaDBContainer;

/**
 * 描述：MariaDB
 * Author：liuxing
 * Date：2020/6/18
 */
public class MariaDB extends TestContainer {

    private MariaDBContainer mariaDB = (MariaDBContainer) new MariaDBContainer("mariadb:10.3")
            .withInitScript("sql/base_test.sql")//指定初始化SQL文件
            .withCommand("--character-set-server=utf8 --collation-server=utf8_unicode_ci")//防止乱码，--skip-grant-tables
            .withExposedPorts(3306);
    ;

    public MariaDB() {
    }

    public MariaDB(TestContainer testContainer) {
        super(testContainer);
    }

    @Override
    public void startContainer() {
        log.info("starting mariadb container ... ");
        mariaDB.start();
        System.setProperty("spring.datasource.url", mariaDB.getJdbcUrl());
        System.setProperty("spring.datasource.driver-class-name", mariaDB.getDriverClassName());
        System.setProperty("spring.datasource.username", mariaDB.getUsername());
        System.setProperty("spring.datasource.password", mariaDB.getPassword());

        log.info("test mariadb url = {}", mariaDB.getJdbcUrl());
        log.info("test mariadb username = {}", mariaDB.getUsername());
        log.info("test mariadb password = {}", mariaDB.getPassword());
        log.info("test mariadb driver = {}", mariaDB.getDriverClassName());

        log.info("mariadb container started !!! ");
    }

    @Override
    public void stopContainer() {
        mariaDB.stop();
    }
}
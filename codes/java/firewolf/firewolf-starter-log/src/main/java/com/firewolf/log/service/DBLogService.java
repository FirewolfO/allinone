package com.firewolf.log.service;

import com.firewolf.log.config.properties.DBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: liuxing
 * Date: 2020/3/1 21:23
 * 把日志放入到数据库中的操作方法
 */
public class DBLogService implements LogService {

    @Autowired
    private DBProperties DBProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询所所有的日志属性
     *
     * @return
     */
    public List<Object> list() {
        String sql = "select * from " + DBProperties.getLogTable();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        List<Object> collect = maps.stream().map(x -> (Object) x).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void save(Object log) {

        try {
            String insertSql = "insert into " + DBProperties.getLogTable() + " (";
            Map<String, Object> columnsMap = getValues(log);
            List<String> columnNames = new ArrayList<>(columnsMap.keySet());
            for (String columnName : columnNames) {
                insertSql += columnName + ",";
            }
            insertSql = insertSql.substring(0, insertSql.length() - 1);
            insertSql = insertSql + ") values (";

            for (int i = 0; i < columnNames.size(); i++) {
                insertSql += "?,";
            }
            insertSql = insertSql.substring(0, insertSql.length() - 1);
            insertSql = insertSql + ")";

            jdbcTemplate.update(insertSql, preparedStatement -> {
                int i = 1;
                for (String columnName : columnNames) {
                    preparedStatement.setObject(i++, columnsMap.get(columnName));
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }

    }


    /**
     * 获取日志对象的属性和值得关联关系
     *
     * @param logPO
     * @return
     */
    private Map<String, Object> getValues(Object logPO) {
        Map<String, Object> columnsMap = new HashMap<>();
        Class<?> aClass = logPO.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Stream.of(declaredFields).forEach(field -> {
            try {
                field.setAccessible(true);
                columnsMap.put(field.getName().replaceAll("[A-Z]", "_$0").toLowerCase(), field.get(logPO));
            } catch (Exception e) {
                System.err.println(e);
            }
        });
        return columnsMap;
    }
}

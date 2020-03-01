package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.config.LogDBProperties;
import com.firewolf.lx.tools.log.LogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:22
 */
public class DefaultDBLogHandler implements LogHandler<LogPO> {

    @Autowired
    private LogDBProperties logDBProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @Override
    public LogPO transLog2SelfObj(LogPO logPO) {
        return logPO;
    }

    @Override
    public void handle(LogPO log) {
        try {
            String insertSql = "insert into " + logDBProperties.getTable() + " (";
            Map<String, Object> columnsMap = getValues(log);
            List<String> columnNames = new ArrayList<>(columnsMap.keySet());
            for (String columnName : columnNames) {
                insertSql += columnName + ",";
            }
            insertSql = insertSql.substring(0,insertSql.length() - 1);
            insertSql = insertSql + ") values (";

            for (int i = 0; i < columnNames.size(); i++) {
                insertSql += "?,";
            }
            insertSql = insertSql.substring(0,insertSql.length() - 1);
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
}

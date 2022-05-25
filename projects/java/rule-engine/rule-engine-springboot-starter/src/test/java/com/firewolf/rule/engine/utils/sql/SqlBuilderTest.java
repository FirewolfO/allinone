package com.firewolf.rule.engine.utils.sql;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SqlBuilderTest {

    @Test
    void buildUnusedDeleteSql() {
    }

    @Test
    void buildUniqueCountSql() {
    }

    @Test
    void buildUniqueQuerySql() {

        String s = SqlBuilder.buildUniqueQuerySql("event_rule_item", Arrays.asList("name"), Arrays.asList("a", "b", "c"), 2);

        assertEquals("SELECT name,CONCAT( a,\",\",b,\",\",c) unikey FROM event_rule_item where (name=:name0 or (a=:a0 and b=:b0 and c=:c0)) or (name=:name1 or (a=:a1 and b=:b1 and c=:c1))", s);

    }

    @Test
    void buildDeleteConflictSql() {
        String s = SqlBuilder.buildDeleteConflictSql("event_rule_item", Arrays.asList("rule_id"), Arrays.asList("event_type", "device_id"),2);
        System.out.println(s);
    }

    @Test
    void buildQuerySql() {
    }

    @Test
    void buildDeleteSql() {
    }

    @Test
    void buildUpdateSql() {
    }

    @Test
    void buildInsertSql() {
    }
}
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

        String s = SqlBuilder.buildUniqueQuerySql("event_rule_item", Arrays.asList("a", "b", "c"), 2);

        assertEquals(s, "SELECT CONCAT( a,\",\",b,\",\",c) unikey FROM event_rule_item where (a=:a0 and b=:b0 and c=:c0) or (a=:a1 and b=:b1 and c=:c1)");

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
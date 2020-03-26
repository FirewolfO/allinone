package com.firewolf.log.utils;

import com.firewolf.log.entity.BaseQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanUtilTest {

    @Test
    void getValueMap() {
    }

    @Test
    void getValue() {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setPageSize(10);
        baseQuery.setCurrentPage(1);
        assertEquals(BeanUtil.getValue(baseQuery, "pageSize"), new Integer(10));
    }
}
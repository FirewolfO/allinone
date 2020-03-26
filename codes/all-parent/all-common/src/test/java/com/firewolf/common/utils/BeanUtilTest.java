package com.firewolf.common.utils;

import com.firewolf.common.entity.BaseQuery;
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
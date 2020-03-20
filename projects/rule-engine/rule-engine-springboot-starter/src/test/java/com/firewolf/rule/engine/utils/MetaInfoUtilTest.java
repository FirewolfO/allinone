package com.firewolf.rule.engine.utils;

import com.firewolf.rule.engine.core.QueryVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class MetaInfoUtilTest {

    @Test
    void getMetaInfo() throws Exception {

        QueryVO queryVO = new QueryVO();
        Field field = MetaInfoUtil.getMetaInfo(queryVO.getClass()).getColumnFieldMap().get("page_size");
        Object o = field.get(queryVO);

        System.out.println(o);


        QueryVO queryVO2 = new QueryVO();
        Field field2 = MetaInfoUtil.getMetaInfo(queryVO2.getClass()).getColumnFieldMap().get("page_size");
        Object o2 = field2.get(queryVO);
        assert o2 != null;

    }

}
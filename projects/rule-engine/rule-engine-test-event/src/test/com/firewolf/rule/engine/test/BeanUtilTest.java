package com.firewolf.rule.engine.test;

import com.firewolf.rule.engine.example.EventRule;
import com.firewolf.rule.engine.example.EventRuleItem;
import com.firewolf.rule.engine.core.EntityMetaInfo;
import com.firewolf.rule.engine.utils.BeanUtil;
import com.firewolf.rule.engine.utils.MetaInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.*;

public class BeanUtilTest {

    public static void main(String[] args) {
        System.out.println(new Random().nextInt(2));
    }

    @Test
    public void test() throws Exception {

        EventRule eventRule = new EventRule();
        eventRule.setId(1);
        eventRule.setName("guize");
        eventRule.setTimePlan("10:20-11:00");
        eventRule.setLinkage("1,3,4");
        eventRule.setIsEnable(1);

        List<String> deviceIds = Arrays.asList("1", "3", "4");
        List<Integer> eventTypes = Arrays.asList(1, 2);
        Integer eventLevel = 1;
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId+new Random().nextInt(1));
                eventRuleItem.setEventLevel(eventLevel+new Random().nextInt(2));
                eventRuleItem.setEventType(eventType+new Random().nextInt(2));
                ruleItems.add(eventRuleItem);
            }
        }
        eventRule.setRuleItems(ruleItems);

        ttt(eventRule, EventRule.class, EventRuleItem.class);

        Map<String, Object> map = BeanUtil.objectToMap(eventRule);
        System.out.println("xxx");
    }


    public void ttt(Object rule, Class<?> mainClazz, Class<?> subClazz) {
        try {
            Map<String, Object> mainParams = BeanUtil.objectToMap(rule);
            Map<String, Object> subParams = new HashMap<>();
            EntityMetaInfo metaInfo = MetaInfoUtil.getMetaInfo(mainClazz);
            String itemFieldName = metaInfo.getItemFieldName();
            Object o = mainParams.get(itemFieldName);
            if (StringUtils.isNotEmpty(itemFieldName)) {
                // 子表数据不用来做主表的查询参数
                mainParams.remove(itemFieldName);
                // 构建子表查询条件
                List list = BeanUtil.transObj2List(o);
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    Map<String, Object> map = BeanUtil.objectToMap(item);
                    BeanUtil.joinMap(subParams, map);
                }

            }

            System.out.println("xxx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

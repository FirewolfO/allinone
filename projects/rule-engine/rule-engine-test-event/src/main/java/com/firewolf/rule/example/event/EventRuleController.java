package com.firewolf.rule.example.event;

import com.firewolf.rule.engine.core.RuleEngine;
import com.firewolf.rule.engine.entity.RuleQuery;
import com.firewolf.rule.engine.utils.MetaInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/example/rule")
@Api(tags = "事件规则操作接口")
public class EventRuleController {

    @Autowired
    @Qualifier("eventRuleEngine")
    private RuleEngine<EventRule, EventRuleItem, LocalDateTime> ruleEngine;

    @ApiOperation("添加规则")
    @PostMapping
    public String add(EventRuleVO exRuleVO) throws Exception {

        EventRule eventRule = new EventRule();
        eventRule.setName(exRuleVO.getName());
        eventRule.setTimePlan(exRuleVO.getTimePlan());
        eventRule.setLinkage(exRuleVO.getLinkage());
        eventRule.setIsEnable(exRuleVO.getIsEnable());
        eventRule.setEventLevel(exRuleVO.getEventLevel());
        List<String> deviceIds = exRuleVO.getDeviceIds();
        List<Integer> eventTypes = exRuleVO.getEventTypes();
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId);

                eventRuleItem.setEventType(eventType);
                ruleItems.add(eventRuleItem);
            }
        }
        eventRule.setRuleItems(ruleItems);
        ruleEngine.addRule(eventRule);
        return "success";
    }


    @ApiOperation("接受处理数据")
    @PostMapping("/match")
    public List<EventRule> receive(Integer eventType, String deviceId) {

        RuleQuery queryVO = new RuleQuery();
        queryVO.getSubParams().put("deviceId", deviceId);
        queryVO.getSubParams().put("eventType", eventType);
//        List<EventRule> eventRules = ruleEngine.matchRules(queryVO, EventRule.class, EventRuleItem.class, LocalDateTime.now());
        List<EventRule> eventRules = ruleEngine.matchRules(queryVO, LocalDateTime.now());
        return eventRules;
    }

    @ApiOperation("删除规则")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        ruleEngine.deleteRule(id);
        return "success";
    }


    @ApiOperation("编辑规则")
    @PutMapping
    public String update(EventRuleVO exRuleVO) throws Exception {

        EventRule eventRule = new EventRule();
        eventRule.setId(exRuleVO.getId());
        eventRule.setName(exRuleVO.getName());
        eventRule.setTimePlan(exRuleVO.getTimePlan());
        eventRule.setLinkage(exRuleVO.getLinkage());
        eventRule.setIsEnable(exRuleVO.getIsEnable());
        eventRule.setEventLevel(exRuleVO.getEventLevel());

        List<String> deviceIds = exRuleVO.getDeviceIds();
        List<Integer> eventTypes = exRuleVO.getEventTypes();
        Integer eventLevel = exRuleVO.getEventLevel();
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId);
                eventRuleItem.setEventType(eventType);
                ruleItems.add(eventRuleItem);
            }
        }
        eventRule.setRuleItems(ruleItems);
        ruleEngine.updateRule(eventRule);
        return "success";
    }

    @ApiOperation("查询规则")
    @GetMapping
    public List<EventRule> list(EventRuleQuery queryVO) throws Exception {

        RuleQuery q = new RuleQuery();
        q.setMainParams(MetaInfoUtil.objectToMapNoNull(queryVO.getMainParams()));
        q.setSubParams(MetaInfoUtil.objectToMapNoNull(queryVO.getSubParams()));
        return ruleEngine.findRules(q);
    }


    @ApiOperation("检查唯一性")
    @PostMapping("check")
    public EventRule check(EventRuleVO exRuleVO) throws Exception {
        EventRule eventRule = new EventRule();
        eventRule.setName(exRuleVO.getName());
        eventRule.setTimePlan(exRuleVO.getTimePlan());
        eventRule.setLinkage(exRuleVO.getLinkage());
        eventRule.setIsEnable(exRuleVO.getIsEnable());
        eventRule.setEventLevel(exRuleVO.getEventLevel());
        List<String> deviceIds = exRuleVO.getDeviceIds();
        List<Integer> eventTypes = exRuleVO.getEventTypes();
        Integer eventLevel = exRuleVO.getEventLevel();
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId);
                eventRuleItem.setEventType(eventType);
                ruleItems.add(eventRuleItem);
            }
        }
        eventRule.setRuleItems(ruleItems);
        return ruleEngine.checkConflict(eventRule);
    }

}

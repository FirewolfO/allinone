package com.firewolf.rule.engine.example;

import com.firewolf.rule.engine.core.QueryVO;
import com.firewolf.rule.engine.utils.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rule/ex")
@Api(tags = "事件规则操作接口")
public class EventRuleController {

    @Autowired
    @Qualifier("eventRuleEngine")
    private EventRuleEngine ruleEngine;

    @ApiOperation("添加规则")
    @PostMapping
    public String add(EventRuleVO exRuleVO) throws Exception {

        EventRule eventRule = new EventRule();
        eventRule.setName(exRuleVO.getName());
        eventRule.setTimePlan(exRuleVO.getTimePlan());
        eventRule.setLinkage(exRuleVO.getLinkage());
        eventRule.setIsEnable(exRuleVO.getIsEnable());

        List<String> deviceIds = exRuleVO.getDeviceIds();
        List<Integer> eventTypes = exRuleVO.getEventTypes();
        Integer eventLevel = exRuleVO.getEventLevel();
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId);
                eventRuleItem.setEventLevel(eventLevel);
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

        QueryVO queryVO = new QueryVO();
        queryVO.getSubParams().put("deviceId", deviceId);
        queryVO.getSubParams().put("eventType", eventType);
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

        List<String> deviceIds = exRuleVO.getDeviceIds();
        List<Integer> eventTypes = exRuleVO.getEventTypes();
        Integer eventLevel = exRuleVO.getEventLevel();
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId);
                eventRuleItem.setEventLevel(eventLevel);
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
    public List<EventRule> list(EventQueryVO queryVO) throws Exception {

        QueryVO q = new QueryVO();
        q.setMainParams(BeanUtil.objectToMap(queryVO.getMainParams()));
        q.setSubParams(BeanUtil.objectToMap(queryVO.getSubParams()));
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
        List<String> deviceIds = exRuleVO.getDeviceIds();
        List<Integer> eventTypes = exRuleVO.getEventTypes();
        Integer eventLevel = exRuleVO.getEventLevel();
        List<EventRuleItem> ruleItems = new ArrayList<>();
        for (String deviceId : deviceIds) {
            for (Integer eventType : eventTypes) {
                EventRuleItem eventRuleItem = new EventRuleItem();
                eventRuleItem.setDeviceId(deviceId);
                eventRuleItem.setEventLevel(eventLevel);
                eventRuleItem.setEventType(eventType);
                ruleItems.add(eventRuleItem);
            }
        }
        eventRule.setRuleItems(ruleItems);
        return ruleEngine.checkConflict(eventRule);
    }

}

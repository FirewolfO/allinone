package com.firewolf.rule.example.cars;

import com.firewolf.rule.engine.core.RuleEngine;
import com.firewolf.rule.engine.entity.RuleQuery;
import com.firewolf.rule.engine.utils.MetaInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/example/car")
@Api(tags = "车辆规则")
public class CarRuleController {

    @Resource
    @Qualifier("carRuleEngine")
    private RuleEngine<CarRule, CarRule, LocalDateTime> ruleEngine;

    @ApiOperation("添加规则")
    @PostMapping
    public String add(CarRule exRuleVO) throws Exception {
        ruleEngine.addRule(exRuleVO);
        return "success";
    }


    @ApiOperation("接受处理数据")
    @PostMapping("/match")
    public List<CarRule> receive(Integer deviceId) {

        RuleQuery queryVO = new RuleQuery();
        queryVO.getMainParams().put("deviceId", deviceId);
        List<CarRule> eventRules = ruleEngine.matchRules(queryVO, CarRule.class, CarRule.class, LocalDateTime.now());
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
    public String update(CarRule exRuleVO) throws Exception {
        ruleEngine.updateRule(exRuleVO);
        return "success";
    }

    @ApiOperation("查询规则")
    @GetMapping
    public List<CarRule> list(CarRule queryVO) throws Exception {
        RuleQuery q = new RuleQuery();
        q.setMainParams(MetaInfoUtil.objectToMapNoNull(queryVO));
        return ruleEngine.findRules(q);
    }


    @ApiOperation("检查唯一性")
    @PostMapping("check")
    public CarRule check(CarRule exRuleVO) throws Exception {
        return ruleEngine.checkConflict(exRuleVO);
    }

}

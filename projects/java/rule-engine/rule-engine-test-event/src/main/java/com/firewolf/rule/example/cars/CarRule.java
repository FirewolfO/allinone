package com.firewolf.rule.example.cars;

import com.firewolf.rule.engine.annotations.Id;
import com.firewolf.rule.engine.annotations.Table;
import lombok.Data;

@Data
@Table("car_rule")
public class CarRule {
    @Id
    private Integer id;
    private String name;
    private Integer deviceId;
    private Integer timePlanId;
}

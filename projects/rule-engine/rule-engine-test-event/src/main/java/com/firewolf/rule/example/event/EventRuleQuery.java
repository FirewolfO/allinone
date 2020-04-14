package com.firewolf.rule.example.event;

import lombok.Data;

@Data
public class EventRuleQuery {


    private MainParams mainParams = new MainParams();

    private SubParams subParams = new SubParams();



}

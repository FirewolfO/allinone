package com.firewolf.rule.engine.enums;

public enum OrderType {
    ASC("asc"),
    DESC("desc");

    OrderType(String description) {
        this.description = description;
    }

    private String description;
    public String getDescription() {
        return description;
    }

}

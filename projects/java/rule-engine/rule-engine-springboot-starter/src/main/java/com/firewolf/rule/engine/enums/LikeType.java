package com.firewolf.rule.engine.enums;

/**
 * like类型
 */
public enum LikeType {
    prefix("'%':%s"), // %aaa
    suffix(":%s'%'"), // aaa%
    all("\'%\':%s\'%\'") // %aaa%
    ;
    private String format;

    LikeType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}

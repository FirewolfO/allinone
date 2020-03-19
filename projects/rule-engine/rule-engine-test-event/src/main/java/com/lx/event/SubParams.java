package com.lx.event;

import lombok.Data;

@Data
public class SubParams {
    private Integer eventType;
    private Integer deviceId;
    private Integer eventLevel;
}
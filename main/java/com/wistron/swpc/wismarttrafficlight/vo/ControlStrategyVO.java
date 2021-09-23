package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ControlStrategyVO {

    @JsonProperty("intersection_id")
    private String intersectionId;

    @JsonProperty("control_strategy")
    private String controlStrategy;

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public String getControlStrategy() {
        return controlStrategy;
    }

    public void setControlStrategy(String controlStrategy) {
        this.controlStrategy = controlStrategy;
    }
}

package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubPhaseDetailVO {

    @JsonProperty("intersection_id")
    private String intersectionId;

    @JsonProperty("control_strategy")
    private String controlStrategy;

    @JsonProperty("control_strategy_from_center")
    private String controlStrategyFromCenter;

    @JsonProperty("sub_phases")
    private List<SubPhaseVO> subPhase;

    @JsonProperty("current_sub_phase_id")
    private String currentSubPhaseId;

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public List<SubPhaseVO> getSubPhase() {
        return subPhase;
    }

    public void setSubPhase(List<SubPhaseVO> subPhase) {
        this.subPhase = subPhase;
    }

    public String getCurrentSubPhaseId() {
        return currentSubPhaseId;
    }

    public void setCurrentSubPhaseId(String currentSubPhaseId) {
        this.currentSubPhaseId = currentSubPhaseId;
    }

    public String getControlStrategy() {
        return controlStrategy;
    }

    public void setControlStrategy(String controlStrategy) {
        this.controlStrategy = controlStrategy;
    }

    public String getControlStrategyFromCenter() {
        return this.controlStrategyFromCenter;
    }

    public void setControlStrategyFromCenter(String controlStrategyFromCenter) {
        this.controlStrategyFromCenter = controlStrategyFromCenter;
    }
}

package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ControlStrategyDTO {

    @JsonProperty("IntersectionID")
    private String intersectionId;

    @JsonProperty("ControlStrategy")
    private String controlStrategy;

    @JsonProperty("Curr_SubPhaseID")
    private String currSubPhaseId;

    @JsonProperty("Curr_Step")
    private String currStep;

    @JsonProperty("Curr_EffectTime")
    private String currEffectTime;

    @JsonProperty("ControlStrategy_From_Center")
    private String controlStrategyFromCenter;

    @JsonProperty("Status_Code")
    private String statusCode;

    /*=========先共用，待移除====================*/
    @JsonProperty("SubPhaseID_1_EffectTime_Total")
    private String subPhaseId1EffectTimeTotal;

    @JsonProperty("SubPhaseID_2_EffectTime_Total")
    private String subPhaseId2EffectTimeTotal;

    @JsonProperty("SubPhaseID_3_EffectTime_Total")
    private String subPhaseId3EffectTimeTotal;

    @JsonProperty("SubPhaseID_4_EffectTime_Total")
    private String subPhaseId4EffectTimeTotal;

    @JsonProperty("SubPhaseID_5_EffectTime_Total")
    private String subPhaseId5EffectTimeTotal;

    @JsonProperty("SubPhaseID_6_EffectTime_Total")
    private String subPhaseId6EffectTimeTotal;

    @JsonProperty("SubPhaseID_7_EffectTime_Total")
    private String subPhaseId7EffectTimeTotal;

    @JsonProperty("SubPhaseID_8_EffectTime_Total")
    private String subPhaseId8EffectTimeTotal;
    /*=========先共用，待移除====================*/

    @JsonProperty("SubPhaseID_1_EffectTime_Green")
    private String subPhaseId1EffectTimeGreen;

    @JsonProperty("SubPhaseID_2_EffectTime_Green")
    private String subPhaseId2EffectTimeGreen;

    @JsonProperty("SubPhaseID_3_EffectTime_Green")
    private String subPhaseId3EffectTimeGreen;

    @JsonProperty("SubPhaseID_4_EffectTime_Green")
    private String subPhaseId4EffectTimeGreen;

    @JsonProperty("SubPhaseID_5_EffectTime_Green")
    private String subPhaseId5EffectTimeGreen;

    @JsonProperty("SubPhaseID_6_EffectTime_Green")
    private String subPhaseId6EffectTimeGreen;

    @JsonProperty("SubPhaseID_7_EffectTime_Green")
    private String subPhaseId7EffectTimeGreen;

    @JsonProperty("SubPhaseID_8_EffectTime_Green")
    private String subPhaseId8EffectTimeGreen;

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

    public String getCurrSubPhaseId() {
        return currSubPhaseId;
    }

    public void setCurrSubPhaseId(String currSubPhaseId) {
        this.currSubPhaseId = currSubPhaseId;
    }

    public String getCurrStep() {
        return currStep;
    }

    public void setCurrStep(String currStep) {
        this.currStep = currStep;
    }

    public String getCurrEffectTime() {
        return currEffectTime;
    }

    public void setCurrEffectTime(String currEffectTime) {
        this.currEffectTime = currEffectTime;
    }

    public String getControlStrategyFromCenter() {
        return controlStrategyFromCenter;
    }

    public void setControlStrategyFromCenter(String controlStrategyFromCenter) {
        this.controlStrategyFromCenter = controlStrategyFromCenter;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSubPhaseId1EffectTimeTotal() {
        return subPhaseId1EffectTimeTotal;
    }

    public void setSubPhaseId1EffectTimeTotal(String subPhaseId1EffectTimeTotal) {
        this.subPhaseId1EffectTimeTotal = subPhaseId1EffectTimeTotal;
    }

    public String getSubPhaseId2EffectTimeTotal() {
        return subPhaseId2EffectTimeTotal;
    }

    public void setSubPhaseId2EffectTimeTotal(String subPhaseId2EffectTimeTotal) {
        this.subPhaseId2EffectTimeTotal = subPhaseId2EffectTimeTotal;
    }

    public String getSubPhaseId3EffectTimeTotal() {
        return subPhaseId3EffectTimeTotal;
    }

    public void setSubPhaseId3EffectTimeTotal(String subPhaseId3EffectTimeTotal) {
        this.subPhaseId3EffectTimeTotal = subPhaseId3EffectTimeTotal;
    }

    public String getSubPhaseId4EffectTimeTotal() {
        return subPhaseId4EffectTimeTotal;
    }

    public void setSubPhaseId4EffectTimeTotal(String subPhaseId4EffectTimeTotal) {
        this.subPhaseId4EffectTimeTotal = subPhaseId4EffectTimeTotal;
    }

    public String getSubPhaseId5EffectTimeTotal() {
        return subPhaseId5EffectTimeTotal;
    }

    public void setSubPhaseId5EffectTimeTotal(String subPhaseId5EffectTimeTotal) {
        this.subPhaseId5EffectTimeTotal = subPhaseId5EffectTimeTotal;
    }

    public String getSubPhaseId6EffectTimeTotal() {
        return subPhaseId6EffectTimeTotal;
    }

    public void setSubPhaseId6EffectTimeTotal(String subPhaseId6EffectTimeTotal) {
        this.subPhaseId6EffectTimeTotal = subPhaseId6EffectTimeTotal;
    }

    public String getSubPhaseId7EffectTimeTotal() {
        return subPhaseId7EffectTimeTotal;
    }

    public void setSubPhaseId7EffectTimeTotal(String subPhaseId7EffectTimeTotal) {
        this.subPhaseId7EffectTimeTotal = subPhaseId7EffectTimeTotal;
    }

    public String getSubPhaseId8EffectTimeTotal() {
        return subPhaseId8EffectTimeTotal;
    }

    public void setSubPhaseId8EffectTimeTotal(String subPhaseId8EffectTimeTotal) {
        this.subPhaseId8EffectTimeTotal = subPhaseId8EffectTimeTotal;
    }

    public String getSubPhaseId1EffectTimeGreen() {
        return this.subPhaseId1EffectTimeGreen;
    }

    public void setSubPhaseId1EffectTimeGreen(String subPhaseId1EffectTimeGreen) {
        this.subPhaseId1EffectTimeGreen = subPhaseId1EffectTimeGreen;
    }

    public String getSubPhaseId2EffectTimeGreen() {
        return this.subPhaseId2EffectTimeGreen;
    }

    public void setSubPhaseId2EffectTimeGreen(String subPhaseId2EffectTimeGreen) {
        this.subPhaseId2EffectTimeGreen = subPhaseId2EffectTimeGreen;
    }

    public String getSubPhaseId3EffectTimeGreen() {
        return this.subPhaseId3EffectTimeGreen;
    }

    public void setSubPhaseId3EffectTimeGreen(String subPhaseId3EffectTimeGreen) {
        this.subPhaseId3EffectTimeGreen = subPhaseId3EffectTimeGreen;
    }

    public String getSubPhaseId4EffectTimeGreen() {
        return this.subPhaseId4EffectTimeGreen;
    }

    public void setSubPhaseId4EffectTimeGreen(String subPhaseId4EffectTimeGreen) {
        this.subPhaseId4EffectTimeGreen = subPhaseId4EffectTimeGreen;
    }

    public String getSubPhaseId5EffectTimeGreen() {
        return this.subPhaseId5EffectTimeGreen;
    }

    public void setSubPhaseId5EffectTimeGreen(String subPhaseId5EffectTimeGreen) {
        this.subPhaseId5EffectTimeGreen = subPhaseId5EffectTimeGreen;
    }

    public String getSubPhaseId6EffectTimeGreen() {
        return this.subPhaseId6EffectTimeGreen;
    }

    public void setSubPhaseId6EffectTimeGreen(String subPhaseId6EffectTimeGreen) {
        this.subPhaseId6EffectTimeGreen = subPhaseId6EffectTimeGreen;
    }

    public String getSubPhaseId7EffectTimeGreen() {
        return this.subPhaseId7EffectTimeGreen;
    }

    public void setSubPhaseId7EffectTimeGreen(String subPhaseId7EffectTimeGreen) {
        this.subPhaseId7EffectTimeGreen = subPhaseId7EffectTimeGreen;
    }

    public String getSubPhaseId8EffectTimeGreen() {
        return this.subPhaseId8EffectTimeGreen;
    }

    public void setSubPhaseId8EffectTimeGreen(String subPhaseId8EffectTimeGreen) {
        this.subPhaseId8EffectTimeGreen = subPhaseId8EffectTimeGreen;
    }
}

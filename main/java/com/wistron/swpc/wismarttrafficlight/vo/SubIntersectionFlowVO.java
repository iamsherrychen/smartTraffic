package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubIntersectionFlowVO {

    @JsonProperty("sub_intersection_name")
    private String subIntersectionName;

    @JsonProperty("sub_intersection_direction")
    private String subIntersectionDirection;

    @JsonProperty("sub_intersection_id")
    private String subIntersectionId;

    @JsonProperty("flow_out_total")
    private double flowOutTotal;

    @JsonProperty("flow_in_total")
    private double flowInTotal;

    @JsonProperty("has_vehicle_detector")
    private boolean hasVehicleDelector;

    @JsonProperty("sub_data")
    private List<SubIntersectionFlowItemVO> subData;

    public String getSubIntersectionDirection() {
        return subIntersectionDirection;
    }

    public void setSubIntersectionDirection(String subIntersectionDirection) {
        this.subIntersectionDirection = subIntersectionDirection;
    }

    public String getSubIntersectionName() {
        return subIntersectionName;
    }

    public void setSubIntersectionName(String subIntersectionName) {
        this.subIntersectionName = subIntersectionName;
    }

    public String getSubIntersectionId() {
        return subIntersectionId;
    }

    public void setSubIntersectionId(String subIntersectionId) {
        this.subIntersectionId = subIntersectionId;
    }

    public double getFlowOutTotal() {
        return flowOutTotal;
    }

    public void setFlowOutTotal(double flowOutTotal) {
        this.flowOutTotal = flowOutTotal;
    }

    public double getFlowInTotal() {
        return flowInTotal;
    }

    public void setFlowInTotal(double flowInTotal) {
        this.flowInTotal = flowInTotal;
    }

    public List<SubIntersectionFlowItemVO> getSubData() {
        return subData;
    }

    public void setSubData(List<SubIntersectionFlowItemVO> subData) {
        this.subData = subData;
    }

    public boolean getHasVehicleDelector() {
        return this.hasVehicleDelector;
    }

    public void setHasVehicleDelector(boolean hasVehicleDelector) {
        this.hasVehicleDelector = hasVehicleDelector;
    }

}

package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubIntersectionFlowItemVO {

    @JsonProperty("connected_sub_intersection_id")
    private String connectedSubId;

    @JsonProperty("connected_sub_intersection_name")
    private String connectedSubName;

    @JsonProperty("connected_sub_intersection_direction")
    private String connectedSubDirection;

    @JsonProperty("flow_out_bigcar")
    private double flowOutBigCar;

    @JsonProperty("flow_out_car")
    private double flowOutCar;

    @JsonProperty("flow_out_motcar")
    private double flowOutMotCar;

    @JsonProperty("flow_out_average_op")
    private double flowOutAvgOp;

    @JsonProperty("flow_in_count")
    private double flowInCount;

    public String getConnectedSubDirection() {
        return connectedSubDirection;
    }

    public void setConnectedSubDirection(String connectedSubDirection) {
        this.connectedSubDirection = connectedSubDirection;
    }

    public String getConnectedSubName() {
        return connectedSubName;
    }

    public void setConnectedSubName(String connectedSubName) {
        this.connectedSubName = connectedSubName;
    }

    public String getConnectedSubId() {
        return connectedSubId;
    }

    public void setConnectedSubId(String connectedSubId) {
        this.connectedSubId = connectedSubId;
    }

    public double getFlowOutBigCar() {
        return flowOutBigCar;
    }

    public void setFlowOutBigCar(double flowOutBigCar) {
        this.flowOutBigCar = flowOutBigCar;
    }

    public double getFlowOutCar() {
        return flowOutCar;
    }

    public void setFlowOutCar(double flowOutCar) {
        this.flowOutCar = flowOutCar;
    }

    public double getFlowOutMotCar() {
        return flowOutMotCar;
    }

    public void setFlowOutMotCar(double flowOutMotCar) {
        this.flowOutMotCar = flowOutMotCar;
    }

    public double getFlowOutAvgOp() {
        return flowOutAvgOp;
    }

    public void setFlowOutAvgOp(double flowOutAvgOp) {
        this.flowOutAvgOp = flowOutAvgOp;
    }

    public double getFlowInCount() {
        return flowInCount;
    }

    public void setFlowInCount(double flowInCount) {
        this.flowInCount = flowInCount;
    }
}

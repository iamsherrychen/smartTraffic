package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubIntersectionFlowItemDTO {

    @JsonProperty("connected_sub_intersection_id")
    private String connectedSubId;

    @JsonProperty("flow_out_bigcar")
    private int flowOutBigCar;

    @JsonProperty("flow_out_car")
    private int flowOutCar;

    @JsonProperty("flow_out_motcar")
    private int flowOutMotCar;

    public String getConnectedSubId() {
        return connectedSubId;
    }

    public void setConnectedSubId(String connectedSubId) {
        this.connectedSubId = connectedSubId;
    }

    public int getFlowOutBigCar() {
        return flowOutBigCar;
    }

    public void setFlowOutBigCar(int flowOutBigCar) {
        this.flowOutBigCar = flowOutBigCar;
    }

    public int getFlowOutCar() {
        return flowOutCar;
    }

    public void setFlowOutCar(int flowOutCar) {
        this.flowOutCar = flowOutCar;
    }

    public int getFlowOutMotCar() {
        return flowOutMotCar;
    }

    public void setFlowOutMotCar(int flowOutMotCar) {
        this.flowOutMotCar = flowOutMotCar;
    }
}

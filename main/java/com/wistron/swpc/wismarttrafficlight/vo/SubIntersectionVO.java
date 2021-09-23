package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubIntersectionVO {

    @JsonProperty("sub_intersection_id")
    private String subIntersectionId;

    private String name;

    @JsonProperty("sub_intersection_direction")
    private String subIntersectionDirection;

    @JsonProperty("flow_in_storage_space")
    private String flowInStorageSpace;

    @JsonProperty("flow_out_storage_space")
    private String flowOutStorageSpace;

    @JsonProperty("flow_in_speed")
    private String flowInSpeed;

    @JsonProperty("flow_out_speed")
    private String flowOutSpeed;

    @JsonProperty("connect_to")
    private String connectTo;

    @JsonProperty("route_to_connected_to")
    private List<RoadPointVO> connectedRoadPoint;

    public List<RoadPointVO> getConnectedRoadPoint() {
        return connectedRoadPoint;
    }

    public void setConnectedRoadPoint(List<RoadPointVO> connectedRoadPoint) {
        this.connectedRoadPoint = connectedRoadPoint;
    }

    public String getSubIntersectionDirection() {
        return subIntersectionDirection;
    }

    public void setSubIntersectionDirection(String subIntersectionDirection) {
        this.subIntersectionDirection = subIntersectionDirection;
    }

    public String getFlowInSpeed() {
        return flowInSpeed;
    }

    public void setFlowInSpeed(String flowInSpeed) {
        this.flowInSpeed = flowInSpeed;
    }

    public String getFlowOutSpeed() {
        return flowOutSpeed;
    }

    public void setFlowOutSpeed(String flowOutSpeed) {
        this.flowOutSpeed = flowOutSpeed;
    }

    public String getConnectTo() {
        return connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    public String getSubIntersectionId() {
        return subIntersectionId;
    }

    public void setSubIntersectionId(String subIntersectionId) {
        this.subIntersectionId = subIntersectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlowInStorageSpace() {
        return flowInStorageSpace;
    }

    public void setFlowInStorageSpace(String flowInStorageSpace) {
        this.flowInStorageSpace = flowInStorageSpace;
    }

    public String getFlowOutStorageSpace() {
        return flowOutStorageSpace;
    }

    public void setFlowOutStorageSpace(String flowOutStorageSpace) {
        this.flowOutStorageSpace = flowOutStorageSpace;
    }
}

package com.wistron.swpc.wismarttrafficlight.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoadSpeedVO {
    @JsonProperty("intersection_id")
    private String intersectionId;

    @JsonProperty("connect_to")
    private String connectTo;

    @JsonProperty("forward_speed_status")
    private String forwardSpeedStatus;

    @JsonProperty("reverse_speed_status")
    private String reverseSpeedStatus;

    @JsonProperty("forward_route_to_connected")
    private List<RoadPointVO> forwardRouteToConnected;

    @JsonProperty("reverse_route_to_connected")
    private List<RoadPointVO> reverseRouteToConnected;

    public String getIntersectionId() {
        return this.intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public String getConnectTo() {
        return this.connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    public String getForwardSpeedStatus() {
        return this.forwardSpeedStatus;
    }

    public void setForwardSpeedStatus(String forwardSpeedStatus) {
        this.forwardSpeedStatus = forwardSpeedStatus;
    }

    public String getReverseSpeedStatus() {
        return this.reverseSpeedStatus;
    }

    public void setReverseSpeedStatus(String reverseSpeedStatus) {
        this.reverseSpeedStatus = reverseSpeedStatus;
    }

    public List<RoadPointVO> getForwardRouteToConnected() {
        return this.forwardRouteToConnected;
    }

    public void setForwardRouteToConnected(List<RoadPointVO> forwardRouteToConnected) {
        this.forwardRouteToConnected = forwardRouteToConnected;
    }

    public List<RoadPointVO> getReverseRouteToConnected() {
        return this.reverseRouteToConnected;
    }

    public void setReverseRouteToConnected(List<RoadPointVO> reverseRouteToConnected) {
        this.reverseRouteToConnected = reverseRouteToConnected;
    }
}
package com.wistron.swpc.wismarttrafficlight.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Intersection {

    @JsonIgnore
    private String uuid;

    private String id;

    private String name;

    @JsonProperty("sub_phase_total")
    private Integer subPhaseTotal;

    @JsonProperty("type_for_light_chart")
    private String typeForLightChart;

    @JsonProperty("type_for_traffic_flow")
    private String typeForTrafficFlow;

    @JsonIgnore
    private String description;

    @JsonIgnore
    private String createdAt;

    @JsonIgnore
    private String updatedAt;

    @JsonProperty("center_longitude")
    private Double centerLongitude;

    @JsonProperty("center_latitude")
    private Double centerLatitude;

    @JsonProperty("popup_position")
    private String popupPosition;

    @JsonProperty("rotate")
    private Integer rotate;

    public String getPopupPosition() {
        return popupPosition;
    }

    public void setPopupPosition(String popupPosition) {
        this.popupPosition = popupPosition;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSubPhaseTotal() {
        return subPhaseTotal;
    }

    public void setSubPhaseTotal(Integer subPhaseTotal) {
        this.subPhaseTotal = subPhaseTotal;
    }

    public String getTypeForLightChart() {
        return typeForLightChart;
    }

    public void setTypeForLightChart(String typeForLightChart) {
        this.typeForLightChart = typeForLightChart;
    }

    public String getTypeForTrafficFlow() {
        return typeForTrafficFlow;
    }

    public void setTypeForTrafficFlow(String typeForTrafficFlow) {
        this.typeForTrafficFlow = typeForTrafficFlow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }


    public Integer getRotate() {
        return this.rotate;
    }

    public void setRotate(Integer rotate) {
        this.rotate = rotate;
    }

}

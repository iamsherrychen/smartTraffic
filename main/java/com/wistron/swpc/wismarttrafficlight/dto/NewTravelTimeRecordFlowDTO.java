package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewTravelTimeRecordFlowDTO {
    @JsonProperty("VehicleType")
    private Integer VehicleType;
    @JsonProperty("VehicleCount")
    private Integer VehicleCount;
    @JsonProperty("TravelTime")
    private Integer TravelTime;
    @JsonProperty("SpaceMeanSpeed")
    private Integer SpaceMeanSpeed;
    @JsonProperty("StandardDeviation")
    private Integer StandardDeviation;


    public Integer getVehicleType() {
        return this.VehicleType;
    }

    public void setVehicleType(Integer VehicleType) {
        this.VehicleType = VehicleType;
    }

    public Integer getVehicleCount() {
        return this.VehicleCount;
    }

    public void setVehicleCount(Integer VehicleCount) {
        this.VehicleCount = VehicleCount;
    }

    public Integer getTravelTime() {
        return this.TravelTime;
    }

    public void setTravelTime(Integer TravelTime) {
        this.TravelTime = TravelTime;
    }

    public Integer getSpaceMeanSpeed() {
        return this.SpaceMeanSpeed;
    }

    public void setSpaceMeanSpeed(Integer SpaceMeanSpeed) {
        this.SpaceMeanSpeed = SpaceMeanSpeed;
    }

    public Integer getStandardDeviation() {
        return this.StandardDeviation;
    }

    public void setStandardDeviation(Integer StandardDeviation) {
        this.StandardDeviation = StandardDeviation;
    }

}

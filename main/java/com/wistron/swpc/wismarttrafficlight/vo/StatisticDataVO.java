package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticDataVO {

    @JsonProperty("intersection_id")
    private String intersectionId;

    @JsonProperty("intersection_name")
    private String intersectionName;

    private double pcu;

    @JsonProperty("car_delay")
    private double carDelay;

    public String getIntersectionName() {
        return intersectionName;
    }

    public void setIntersectionName(String intersectionName) {
        this.intersectionName = intersectionName;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public double getPcu() {
        return pcu;
    }

    public void setPcu(double pcu) {
        this.pcu = pcu;
    }


    public double getCarDelay() {
        return this.carDelay;
    }

    public void setCarDelay(double carDelay) {
        this.carDelay = carDelay;
    }

}

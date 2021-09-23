package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubIntersectionFlowDTO {

    @JsonProperty("DeviceID")
    private String deviceId;

    @JsonProperty("DataTime")
    private String dataTime;

    @JsonProperty("sub_intersection_id")
    private String subIntersectionId;

    @JsonProperty("average_speed")
    private double avgSpeed;

    @JsonProperty("quflow_arrive")
    private double quflowArrive;

    @JsonProperty("car_storage_status")
    private double carStorageStatus;

    @JsonProperty("bigcardelay")
    private double bigCarDelay;

    @JsonProperty("cardelay")
    private double carDelay;

    @JsonProperty("motordelay")
    private double motorDelay;


    @JsonProperty("sub_data")
    private List<SubIntersectionFlowItemDTO> subData;


    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getDataTime() {
        return this.dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
    


    public double getCarStorageStatus() {
        return carStorageStatus;
    }

    public void setCarStorageStatus(double carStorageStatus) {
        this.carStorageStatus = carStorageStatus;
    }

    public String getSubIntersectionId() {
        return subIntersectionId;
    }

    public void setSubIntersectionId(String subIntersectionId) {
        this.subIntersectionId = subIntersectionId;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getQuflowArrive() {
        return quflowArrive;
    }

    public void setQuflowArrive(double quflowArrive) {
        this.quflowArrive = quflowArrive;
    }


    public double getBigCarDelay() {
        return this.bigCarDelay;
    }

    public void setBigCarDelay(double bigCarDelay) {
        this.bigCarDelay = bigCarDelay;
    }

    public double getCarDelay() {
        return this.carDelay;
    }

    public void setCarDelay(double carDelay) {
        this.carDelay = carDelay;
    }

    public double getMotorDelay() {
        return this.motorDelay;
    }

    public void setMotorDelay(double motorDelay) {
        this.motorDelay = motorDelay;
    }


    public List<SubIntersectionFlowItemDTO> getSubData() {
        return subData;
    }

    public void setSubData(List<SubIntersectionFlowItemDTO> subData) {
        this.subData = subData;
    }
}

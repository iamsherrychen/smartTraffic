package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeStatisticVO<T> {

    @JsonProperty("time_point")
    private String timePoint;

    //大園案
    @JsonProperty("speed_east")
    private double speedEast;

    @JsonProperty("speed_west")
    private double speedWest;

    //大竹案 - 大竹中正東路直行
    @JsonProperty("speed_dazu_zzer_forward")
    private double speedDazuZzerForward;

    @JsonProperty("speed_dazu_zzer_reverse")
    private double speedDazuZzerReverse;

    //大竹案 - 台31線上下匝道
    @JsonProperty("speed_dazu_nqr_forward")
    private double speedDazuNqrForward;

    @JsonProperty("speed_dazu_nqr_reverse")
    private double speedDazuNqrReverse;

    //大竹案 - 台31線與中正東路正反向轉彎
    @JsonProperty("speed_dazu_nqr_turn_zzer_forward")
    private double speedDazuNqrTurnZzerForward;

    @JsonProperty("speed_dazu_nqr_turn_zzer_reverse")
    private double speedDazuNqrTurnZzerReverse;

    @JsonProperty("travel_time_east")
    private Integer travelTimeEast;

    @JsonProperty("travel_time_west")
    private Integer travelTimeWest;

    //大竹案 - 大竹中正東路直行
    @JsonProperty("travel_time_dazu_zzer_forward")
    private double travelTimeDazuZzerForward;

    @JsonProperty("travel_time_dazu_zzer_reverse")
    private double travelTimeDazuZzerReverse;

    //大竹案 - 台31線上下匝道
    @JsonProperty("travel_time_dazu_nqr_forward")
    private double travelTimeDazuNqrForward;

    @JsonProperty("travel_time_dazu_nqr_reverse")
    private double travelTimeDazuNqrReverse;

    //大竹案 - 台31線與中正東路正反向轉彎
    @JsonProperty("travel_time_dazu_nqr_turn_zzer_forward")
    private double travelTimeDazuNqrTurnZzerForward;

    @JsonProperty("travel_time_dazu_nqr_turn_zzer_reverse")
    private double travelTimeDazuNqrTurnZzerReverse;


    @JsonProperty("statistic_data")
    private T statisticData;

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }


    public Double getSpeedEast() {
        return this.speedEast;
    }

    public void setSpeedEast(double speedEast) {
        this.speedEast = speedEast;
    }

    public double getSpeedWest() {
        return this.speedWest;
    }

    public void setSpeedWest(double speedWest) {
        this.speedWest = speedWest;
    }

    public T getStatisticData() {
        return statisticData;
    }

    public void setStatisticData(T statisticData) {
        this.statisticData = statisticData;
    }

    public Integer getTravelTimeEast() {
        return this.travelTimeEast;
    }

    public void setTravelTimeEast(Integer travelTimeEast) {
        this.travelTimeEast = travelTimeEast;
    }

    public Integer getTravelTimeWest() {
        return this.travelTimeWest;
    }

    public void setTravelTimeWest(Integer travelTimeWest) {
        this.travelTimeWest = travelTimeWest;
    }


    public double getSpeedDazuZzerForward() {
        return this.speedDazuZzerForward;
    }

    public void setSpeedDazuZzerForward(double speedDazuZzerForward) {
        this.speedDazuZzerForward = speedDazuZzerForward;
    }

    public double getSpeedDazuZzerReverse() {
        return this.speedDazuZzerReverse;
    }

    public void setSpeedDazuZzerReverse(double speedDazuZzerReverse) {
        this.speedDazuZzerReverse = speedDazuZzerReverse;
    }

    public double getSpeedDazuNqrForward() {
        return this.speedDazuNqrForward;
    }

    public void setSpeedDazuNqrForward(double speedDazuNqrForward) {
        this.speedDazuNqrForward = speedDazuNqrForward;
    }

    public double getSpeedDazuNqrReverse() {
        return this.speedDazuNqrReverse;
    }

    public void setSpeedDazuNqrReverse(double speedDazuNqrReverse) {
        this.speedDazuNqrReverse = speedDazuNqrReverse;
    }

    public double getSpeedDazuNqrTurnZzerForward() {
        return this.speedDazuNqrTurnZzerForward;
    }

    public void setSpeedDazuNqrTurnZzerForward(double speedDazuNqrTurnZzerForward) {
        this.speedDazuNqrTurnZzerForward = speedDazuNqrTurnZzerForward;
    }

    public double getSpeedDazuNqrTurnZzerReverse() {
        return this.speedDazuNqrTurnZzerReverse;
    }

    public void setSpeedDazuNqrTurnZzerReverse(double speedDazuNqrTurnZzerReverse) {
        this.speedDazuNqrTurnZzerReverse = speedDazuNqrTurnZzerReverse;
    }

    public double getTravelTimeDazuZzerForward() {
        return this.travelTimeDazuZzerForward;
    }

    public void setTravelTimeDazuZzerForward(double travelTimeDazuZzerForward) {
        this.travelTimeDazuZzerForward = travelTimeDazuZzerForward;
    }

    public double getTravelTimeDazuZzerReverse() {
        return this.travelTimeDazuZzerReverse;
    }

    public void setTravelTimeDazuZzerReverse(double travelTimeDazuZzerReverse) {
        this.travelTimeDazuZzerReverse = travelTimeDazuZzerReverse;
    }

    public double getTravelTimeDazuNqrForward() {
        return this.travelTimeDazuNqrForward;
    }

    public void setTravelTimeDazuNqrForward(double travelTimeDazuNqrForward) {
        this.travelTimeDazuNqrForward = travelTimeDazuNqrForward;
    }

    public double getTravelTimeDazuNqrReverse() {
        return this.travelTimeDazuNqrReverse;
    }

    public void setTravelTimeDazuNqrReverse(double travelTimeDazuNqrReverse) {
        this.travelTimeDazuNqrReverse = travelTimeDazuNqrReverse;
    }

    public double getTravelTimeDazuNqrTurnZzerForward() {
        return this.travelTimeDazuNqrTurnZzerForward;
    }

    public void setTravelTimeDazuNqrTurnZzerForward(double travelTimeDazuNqrTurnZzerForward) {
        this.travelTimeDazuNqrTurnZzerForward = travelTimeDazuNqrTurnZzerForward;
    }

    public double getTravelTimeDazuNqrTurnZzerReverse() {
        return this.travelTimeDazuNqrTurnZzerReverse;
    }

    public void setTravelTimeDazuNqrTurnZzerReverse(double travelTimeDazuNqrTurnZzerReverse) {
        this.travelTimeDazuNqrTurnZzerReverse = travelTimeDazuNqrTurnZzerReverse;
    }

}

package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AreaStatisticVO {

    //大園案
    @JsonProperty("average_car_speed_east")
    private Double avgCarSpeedEast;

    @JsonProperty("average_car_speed_west")
    private Double avgCarSpeedWest;

    //大竹案 - 大竹中正東路直行
    @JsonProperty("average_car_speed_dazu_zzer_forward")
    private Double avgCarSpeedDazuZzerForward;

    @JsonProperty("average_car_speed_dazu_zzer_reverse")
    private Double avgCarSpeedDazuZzerReverse;

    //大竹案 - 台31線上下匝道
    @JsonProperty("average_car_speed_dazu_nqr_forward")
    private Double avgCarSpeedDazuNqrForward;

    @JsonProperty("average_car_speed_dazu_nqr_reverse")
    private Double avgCarSpeedDazuNqrReverse;

    //大竹案 - 台31線與中正東路正反向轉彎
    @JsonProperty("average_car_speed_dazu_nqr_turn_zzer_forward")
    private Double avgCarSpeedDazuNqrTurnZzerForward;

    @JsonProperty("average_car_speed_dazu_nqr_turn_zzer_reverse")
    private Double avgCarSpeedDazuNqrTurnZzerReverse;

    //大園案
    @JsonProperty("travel_time_east")
    private Integer travelTimeEast;

    @JsonProperty("travel_time_west")
    private Integer travelTimeWest;

    //大竹案 - 大竹中正東路直行
    @JsonProperty("travel_time_dazu_zzer_forward")
    private Integer travelTimeDazuZzerForward;

    @JsonProperty("travel_time_dazu_zzer_reverse")
    private Integer travelTimeDazuZzerReverse;

    //大竹案 - 台31線上下匝道
    @JsonProperty("travel_time_dazu_nqr_forward")
    private Integer travelTimeDazuNqrForward;

    @JsonProperty("travel_time_dazu_nqr_reverse")
    private Integer travelTimeDazuNqrReverse;

    //大竹案 - 台31線與中正東路正反向轉彎
    @JsonProperty("travel_time_dazu_nqr_turn_zzer_forward")
    private Integer travelTimeDazuNqrTurnZzerForward;

    @JsonProperty("travel_time_dazu_nqr_turn_zzer_reverse")
    private Integer travelTimeDazuNqrTurnZzerReverse;

    public Double getAvgCarSpeedEast() {
        return avgCarSpeedEast;
    }

    public void setAvgCarSpeedEast(Double avgCarSpeedEast) {
        this.avgCarSpeedEast = avgCarSpeedEast;
    }

    public Double getAvgCarSpeedWest() {
        return avgCarSpeedWest;
    }

    public void setAvgCarSpeedWest(Double avgCarSpeedWest) {
        this.avgCarSpeedWest = avgCarSpeedWest;
    }

    public Integer getTravelTimeEast() {
        return travelTimeEast;
    }

    public void setTravelTimeEast(Integer travelTimeEast) {
        this.travelTimeEast = travelTimeEast;
    }

    public Integer getTravelTimeWest() {
        return travelTimeWest;
    }

    public void setTravelTimeWest(Integer travelTimeWest) {
        this.travelTimeWest = travelTimeWest;
    }


    public Double getAvgCarSpeedDazuZzerForward() {
        return this.avgCarSpeedDazuZzerForward;
    }

    public void setAvgCarSpeedDazuZzerForward(Double avgCarSpeedDazuZzerForward) {
        this.avgCarSpeedDazuZzerForward = avgCarSpeedDazuZzerForward;
    }

    public Double getAvgCarSpeedDazuZzerReverse() {
        return this.avgCarSpeedDazuZzerReverse;
    }

    public void setAvgCarSpeedDazuZzerReverse(Double avgCarSpeedDazuZzerReverse) {
        this.avgCarSpeedDazuZzerReverse = avgCarSpeedDazuZzerReverse;
    }

    public Double getAvgCarSpeedDazuNqrForward() {
        return this.avgCarSpeedDazuNqrForward;
    }

    public void setAvgCarSpeedDazuNqrForward(Double avgCarSpeedDazuNqrForward) {
        this.avgCarSpeedDazuNqrForward = avgCarSpeedDazuNqrForward;
    }

    public Double getAvgCarSpeedDazuNqrReverse() {
        return this.avgCarSpeedDazuNqrReverse;
    }

    public void setAvgCarSpeedDazuNqrReverse(Double avgCarSpeedDazuNqrReverse) {
        this.avgCarSpeedDazuNqrReverse = avgCarSpeedDazuNqrReverse;
    }

    public Double getAvgCarSpeedDazuNqrTurnZzerForward() {
        return this.avgCarSpeedDazuNqrTurnZzerForward;
    }

    public void setAvgCarSpeedDazuNqrTurnZzerForward(Double avgCarSpeedDazuNqrTurnZzerForward) {
        this.avgCarSpeedDazuNqrTurnZzerForward = avgCarSpeedDazuNqrTurnZzerForward;
    }

    public Double getAvgCarSpeedDazuNqrTurnZzerReverse() {
        return this.avgCarSpeedDazuNqrTurnZzerReverse;
    }

    public void setAvgCarSpeedDazuNqrTurnZzerReverse(Double avgCarSpeedDazuNqrTurnZzerReverse) {
        this.avgCarSpeedDazuNqrTurnZzerReverse = avgCarSpeedDazuNqrTurnZzerReverse;
    }


    public Integer getTravelTimeDazuZzerForward() {
        return this.travelTimeDazuZzerForward;
    }

    public void setTravelTimeDazuZzerForward(Integer travelTimeDazuZzerForward) {
        this.travelTimeDazuZzerForward = travelTimeDazuZzerForward;
    }

    public Integer getTravelTimeDazuZzerReverse() {
        return this.travelTimeDazuZzerReverse;
    }

    public void setTravelTimeDazuZzerReverse(Integer travelTimeDazuZzerReverse) {
        this.travelTimeDazuZzerReverse = travelTimeDazuZzerReverse;
    }

    public Integer getTravelTimeDazuNqrForward() {
        return this.travelTimeDazuNqrForward;
    }

    public void setTravelTimeDazuNqrForward(Integer travelTimeDazuNqrForward) {
        this.travelTimeDazuNqrForward = travelTimeDazuNqrForward;
    }

    public Integer getTravelTimeDazuNqrReverse() {
        return this.travelTimeDazuNqrReverse;
    }

    public void setTravelTimeDazuNqrReverse(Integer travelTimeDazuNqrReverse) {
        this.travelTimeDazuNqrReverse = travelTimeDazuNqrReverse;
    }

    public Integer getTravelTimeDazuNqrTurnZzerForward() {
        return this.travelTimeDazuNqrTurnZzerForward;
    }

    public void setTravelTimeDazuNqrTurnZzerForward(Integer travelTimeDazuNqrTurnZzerForward) {
        this.travelTimeDazuNqrTurnZzerForward = travelTimeDazuNqrTurnZzerForward;
    }

    public Integer getTravelTimeDazuNqrTurnZzerReverse() {
        return this.travelTimeDazuNqrTurnZzerReverse;
    }

    public void setTravelTimeDazuNqrTurnZzerReverse(Integer travelTimeDazuNqrTurnZzerReverse) {
        this.travelTimeDazuNqrTurnZzerReverse = travelTimeDazuNqrTurnZzerReverse;
    }


}

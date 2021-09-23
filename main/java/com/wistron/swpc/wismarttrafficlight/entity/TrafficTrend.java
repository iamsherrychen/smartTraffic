package com.wistron.swpc.wismarttrafficlight.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("traffic_trend")
public class TrafficTrend {

    @Id
    private ObjectId id;

    @Field("intersection_id")
    private String intersectionId;

    //大竹案 - 大竹中正東路直行
    @Field("speed_dazu_zzer_forward")
    private Double speedDazuZzerForward;

    @Field("speed_dazu_zzer_reverse")
    private Double speedDazuZzerReverse;

    //大竹案 - 台31線上下匝道
    @Field("speed_dazu_nqr_forward")
    private Double speedDazuNqrForward;

    @Field("speed_dazu_nqr_reverse")
    private Double speedDazuNqrReverse;

    //大竹案 - 台31線與中正東路正反向轉彎
    @Field("speed_dazu_nqr_turn_zzer_forward")
    private Double speedDazuNqrTurnZzerForward;

    @Field("speed_dazu_nqr_turn_zzer_reverse")
    private Double speedDazuNqrTurnZzerReverse;

    @Field("speed_east")
    private Double speedEast;

    @Field("speed_west")
    private Double speedWest;

    @Field("travel_time_east")
    private Integer travelTimeEast;

    @Field("travel_time_west")
    private Integer travelTimeWest;

    //大竹案 - 大竹中正東路直行
    @Field("travel_time_dazu_zzer_forward")
    private Integer travelTimeDazuZzerForward;

    @Field("travel_time_dazu_zzer_reverse")
    private Integer travelTimeDazuZzerReverse;

    //大竹案 - 台31線上下匝道
    @Field("travel_time_dazu_nqr_forward")
    private Integer travelTimeDazuNqrForward;

    @Field("travel_time_dazu_nqr_reverse")
    private Integer travelTimeDazuNqrReverse;

    //大竹案 - 台31線與中正東路正反向轉彎
    @Field("travel_time_dazu_nqr_turn_zzer_forward")
    private Integer travelTimeDazuNqrTurnZzerForward;

    @Field("travel_time_dazu_nqr_turn_zzer_reverse")
    private Integer travelTimeDazuNqrTurnZzerReverse;

    private Double pcu;

    @Field("car_delay")
    private Double carDelay;

    private int hour;

    /**
     * ipc 传过来的原始数据
     */
    @Field("ipc_msg")
    private String ipcMsg;

    @Field("create_at")
    private Date createAt;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public Double getSpeedEast() {
        return this.speedEast;
    }

    public void setSpeedEast(Double speedEast) {
        this.speedEast = speedEast;
    }

    public Double getSpeedWest() {
        return this.speedWest;
    }

    public void setSpeedWest(Double speedWest) {
        this.speedWest = speedWest;
    }


    public Double getSpeedDazuZzerForward() {
        return this.speedDazuZzerForward;
    }

    public void setSpeedDazuZzerForward(Double speedDazuZzerForward) {
        this.speedDazuZzerForward = speedDazuZzerForward;
    }

    public Double getSpeedDazuZzerReverse() {
        return this.speedDazuZzerReverse;
    }

    public void setSpeedDazuZzerReverse(Double speedDazuZzerReverse) {
        this.speedDazuZzerReverse = speedDazuZzerReverse;
    }

    public Double getSpeedDazuNqrForward() {
        return this.speedDazuNqrForward;
    }

    public void setSpeedDazuNqrForward(Double speedDazuNqrForward) {
        this.speedDazuNqrForward = speedDazuNqrForward;
    }

    public Double getSpeedDazuNqrReverse() {
        return this.speedDazuNqrReverse;
    }

    public void setSpeedDazuNqrReverse(Double speedDazuNqrReverse) {
        this.speedDazuNqrReverse = speedDazuNqrReverse;
    }

    public Double getSpeedDazuNqrTurnZzerForward() {
        return this.speedDazuNqrTurnZzerForward;
    }

    public void setSpeedDazuNqrTurnZzerForward(Double speedDazuNqrTurnZzerForward) {
        this.speedDazuNqrTurnZzerForward = speedDazuNqrTurnZzerForward;
    }

    public Double getSpeedDazuNqrTurnZzerReverse() {
        return this.speedDazuNqrTurnZzerReverse;
    }

    public void setSpeedDazuNqrTurnZzerReverse(Double speedDazuNqrTurnZzerReverse) {
        this.speedDazuNqrTurnZzerReverse = speedDazuNqrTurnZzerReverse;
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
    

    public Double getPcu() {
        return pcu;
    }

    public void setPcu(Double pcu) {
        this.pcu = pcu;
    }

    public Double getCarDelay() {
        return this.carDelay;
    }

    public void setCarDelay(Double carDelay) {
        this.carDelay = carDelay;
    }



    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getIpcMsg() {
        return ipcMsg;
    }

    public void setIpcMsg(String ipcMsg) {
        this.ipcMsg = ipcMsg;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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

}

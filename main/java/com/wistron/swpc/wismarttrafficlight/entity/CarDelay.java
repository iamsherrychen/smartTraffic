package com.wistron.swpc.wismarttrafficlight.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("car_delay")
public class CarDelay {

    @Id
    private ObjectId id;

    @Field("intersection_id")
    private String intersectionId;

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
}

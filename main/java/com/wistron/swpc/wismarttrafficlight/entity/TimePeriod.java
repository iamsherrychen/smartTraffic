package com.wistron.swpc.wismarttrafficlight.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimePeriod {
    
    private String uuid;

    @JsonProperty("intersection_id")
    private String intersectionId;

    @JsonProperty("from_hour_time")
    private String fromHourTime;

    @JsonProperty("to_hour_time")
    private String toHourTime;

    @JsonProperty("timing_uuid")
    private String timingUuid;


    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIntersectionId() {
        return this.intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public String getFromHourTime() {
        return this.fromHourTime;
    }

    public void setFromHourTime(String fromHourTime) {
        this.fromHourTime = fromHourTime;
    }

    public String getToHourTime() {
        return this.toHourTime;
    }

    public void setToHourTime(String toHourTime) {
        this.toHourTime = toHourTime;
    }

    public String getTimingUuid() {
        return this.timingUuid;
    }

    public void setTimingUuid(String timingUuid) {
        this.timingUuid = timingUuid;
    }

}
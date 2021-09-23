package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TravelTimeRecordDTO {

    @JsonProperty("_id")
    private Integer id;
    private String avipairid;
    private String startavistatus;
    private String endavistatus;
    private String traveltime;
    private String datacollecttime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvipairid() {
        return avipairid;
    }

    public void setAvipairid(String avipairid) {
        this.avipairid = avipairid;
    }

    public String getStartavistatus() {
        return startavistatus;
    }

    public void setStartavistatus(String startavistatus) {
        this.startavistatus = startavistatus;
    }

    public String getEndavistatus() {
        return endavistatus;
    }

    public void setEndavistatus(String endavistatus) {
        this.endavistatus = endavistatus;
    }

    public String getTraveltime() {
        return traveltime;
    }

    public void setTraveltime(String traveltime) {
        this.traveltime = traveltime;
    }

    public String getDatacollecttime() {
        return datacollecttime;
    }

    public void setDatacollecttime(String datacollecttime) {
        this.datacollecttime = datacollecttime;
    }
}

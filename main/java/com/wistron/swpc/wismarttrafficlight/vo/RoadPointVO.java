package com.wistron.swpc.wismarttrafficlight.vo;

public class RoadPointVO {

    private double latitude;
    private double longitude;

    public RoadPointVO() {
    }

    public RoadPointVO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}

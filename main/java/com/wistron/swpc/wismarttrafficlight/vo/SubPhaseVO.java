package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubPhaseVO {

    @JsonProperty("sub_phase_id")
    private String subPhaseId;

    @JsonProperty("effect_time_total")
    private Integer effectTimeTotal;

    @JsonProperty("effect_time_last")
    private Integer effectTimeLast;

    @JsonProperty("sub_intersection_1_light")
    private String subIntersection1Light;

    @JsonProperty("sub_intersection_2_light")
    private String subIntersection2Light;

    @JsonProperty("sub_intersection_3_light")
    private String subIntersection3Light;

    @JsonProperty("sub_intersection_4_light")
    private String subIntersection4Light;

    @JsonProperty("sub_intersection_5_light")
    private String subIntersection5Light;

    @JsonProperty("sub_intersection_6_light")
    private String subIntersection6Light;

    @JsonProperty("sub_intersection_7_light")
    private String subIntersection7Light;

    @JsonProperty("sub_intersection_8_light")
    private String subIntersection8Light;

    public String getSubPhaseId() {
        return subPhaseId;
    }

    public void setSubPhaseId(String subPhaseId) {
        this.subPhaseId = subPhaseId;
    }

    public Integer getEffectTimeTotal() {
        return effectTimeTotal;
    }

    public void setEffectTimeTotal(Integer effectTimeTotal) {
        this.effectTimeTotal = effectTimeTotal;
    }

    public String getSubIntersection1Light() {
        return subIntersection1Light;
    }

    public void setSubIntersection1Light(String subIntersection1Light) {
        this.subIntersection1Light = subIntersection1Light;
    }

    public String getSubIntersection2Light() {
        return subIntersection2Light;
    }

    public void setSubIntersection2Light(String subIntersection2Light) {
        this.subIntersection2Light = subIntersection2Light;
    }

    public String getSubIntersection3Light() {
        return subIntersection3Light;
    }

    public void setSubIntersection3Light(String subIntersection3Light) {
        this.subIntersection3Light = subIntersection3Light;
    }

    public String getSubIntersection4Light() {
        return subIntersection4Light;
    }

    public void setSubIntersection4Light(String subIntersection4Light) {
        this.subIntersection4Light = subIntersection4Light;
    }

    public String getSubIntersection5Light() {
        return subIntersection5Light;
    }

    public void setSubIntersection5Light(String subIntersection5Light) {
        this.subIntersection5Light = subIntersection5Light;
    }

    public String getSubIntersection6Light() {
        return subIntersection6Light;
    }

    public void setSubIntersection6Light(String subIntersection6Light) {
        this.subIntersection6Light = subIntersection6Light;
    }

    public String getSubIntersection7Light() {
        return subIntersection7Light;
    }

    public void setSubIntersection7Light(String subIntersection7Light) {
        this.subIntersection7Light = subIntersection7Light;
    }

    public String getSubIntersection8Light() {
        return subIntersection8Light;
    }

    public void setSubIntersection8Light(String subIntersection8Light) {
        this.subIntersection8Light = subIntersection8Light;
    }

    public Integer getEffectTimeLast() {
        return this.effectTimeLast;
    }

    public void setEffectTimeLast(Integer effectTimeLast) {
        this.effectTimeLast = effectTimeLast;
    }

}

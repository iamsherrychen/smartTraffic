package com.wistron.swpc.wismarttrafficlight.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubPhase {

    private String uuid;

    @JsonProperty("timing_uuid")
    private String timingUuid;

    private String id;

    @JsonProperty("effect_time_step_1")
    private Integer effectTimeStep1;

    @JsonProperty("effect_time_step_2")
    private Integer effectTimeStep2;

    @JsonProperty("effect_time_step_3")
    private Integer effectTimeStep3;

    @JsonProperty("effect_time_step_4")
    private Integer effectTimeStep4;

    @JsonProperty("effect_time_step_5")
    private Integer effectTimeStep5;

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

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    public SubPhase() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimingUuid() {
        return this.timingUuid;
    }

    public void setTimingUuid(String timingUuid) {
        this.timingUuid = timingUuid;
    }    
    
    public Integer getEffectTimeStep1() {
        return effectTimeStep1 == null ? 0 : effectTimeStep1;
    }

    public void setEffectTimeStep1(Integer effectTimeStep1) {
        this.effectTimeStep1 = effectTimeStep1;
    }

    public Integer getEffectTimeStep2() {
        return effectTimeStep2 == null ? 0 : effectTimeStep2;
    }

    public void setEffectTimeStep2(Integer effectTimeStep2) {
        this.effectTimeStep2 = effectTimeStep2;
    }

    public Integer getEffectTimeStep3() {
        return effectTimeStep3 == null ? 0 : effectTimeStep3;
    }

    public void setEffectTimeStep3(Integer effectTimeStep3) {
        this.effectTimeStep3 = effectTimeStep3;
    }

    public Integer getEffectTimeStep4() {
        return effectTimeStep4 == null ? 0 : effectTimeStep4;
    }

    public void setEffectTimeStep4(Integer effectTimeStep4) {
        this.effectTimeStep4 = effectTimeStep4;
    }

    public Integer getEffectTimeStep5() {
        return effectTimeStep5 == null ? 0 : effectTimeStep5;
    }

    public void setEffectTimeStep5(Integer effectTimeStep5) {
        this.effectTimeStep5 = effectTimeStep5;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

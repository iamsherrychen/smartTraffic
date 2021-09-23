package com.wistron.swpc.wismarttrafficlight.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorageSpeedVO {

    @JsonProperty("intersection_id")
    private String intersectionId;

    @JsonProperty("sub_intersections")
    private List<SubIntersectionVO> subIntersections;

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public List<SubIntersectionVO> getSubIntersections() {
        return subIntersections;
    }

    public void setSubIntersections(List<SubIntersectionVO> subIntersections) {
        this.subIntersections = subIntersections;
    }
}

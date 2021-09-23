package com.wistron.swpc.wismarttrafficlight.entity.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntersectionType {
    @JsonProperty("intersection_id ")
    private String intersectionId; 

    @JsonProperty("date")
    private String date;

    @JsonProperty("document_file_url")
    private String documentFileUrl;
    


    public String getIntersectionId() {
        return this.intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDocumentFileUrl() {
        return this.documentFileUrl;
    }

    public void setDocumentFileUrl(String documentFileUrl) {
        this.documentFileUrl = documentFileUrl;
    }

}

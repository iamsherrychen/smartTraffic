package com.wistron.swpc.wismarttrafficlight.entity.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectType {
    @JsonProperty("project_id ")
    private String projectId; 

    @JsonProperty("date")
    private String date;

    @JsonProperty("document_file_url")
    private String documentFileUrl;


    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

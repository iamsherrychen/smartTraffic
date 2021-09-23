package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewTravelTimeRecordDTO {
    @JsonProperty("ETagPairID")
    private String ETagPairID;
    @JsonProperty("AuthorityCode")
    private String AuthorityCode;
    @JsonProperty("DataCollectTime")
    private String DataCollectTime;
    @JsonProperty("StartETagStatus")
    private String StartETagStatus;
    @JsonProperty("EndETagStatus")
    private String EndETagStatus;
    @JsonProperty("StartTime")
    private String StartTime;
    @JsonProperty("EndTime")
    private String EndTime;
    @JsonProperty("UpdateTime")
    private String UpdateTime;
    @JsonProperty("Flows")
    private NewTravelTimeRecordFlowDTO[] Flows;


    public String getETagPairID() {
        return this.ETagPairID;
    }

    public void setETagPairID(String ETagPairID) {
        this.ETagPairID = ETagPairID;
    }

    public String getAuthorityCode() {
        return this.AuthorityCode;
    }

    public void setAuthorityCode(String AuthorityCode) {
        this.AuthorityCode = AuthorityCode;
    }

    public String getDataCollectTime() {
        return this.DataCollectTime;
    }

    public void setDataCollectTime(String DataCollectTime) {
        this.DataCollectTime = DataCollectTime;
    }

    public String getStartETagStatus() {
        return this.StartETagStatus;
    }

    public void setStartETagStatus(String StartETagStatus) {
        this.StartETagStatus = StartETagStatus;
    }

    public String getEndETagStatus() {
        return this.EndETagStatus;
    }

    public void setEndETagStatus(String EndETagStatus) {
        this.EndETagStatus = EndETagStatus;
    }

    public String getStartTime() {
        return this.StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return this.EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getUpdateTime() {
        return this.UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public NewTravelTimeRecordFlowDTO[] getFlows() {
        return this.Flows;
    }

    public void setFlows(NewTravelTimeRecordFlowDTO[] Flows) {
        this.Flows = Flows;
    }

}

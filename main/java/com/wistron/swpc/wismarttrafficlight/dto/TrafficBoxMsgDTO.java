package com.wistron.swpc.wismarttrafficlight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * traffic box 消息交互实体类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrafficBoxMsgDTO {

    @JsonProperty("MsgType")
    private String msgType;  // 交互消息类型

    @JsonProperty("Cmd")
    private String cmd; // 请求消息类型

    @JsonProperty("MsgSEQ")
    private String msgSeq; // 消息唯一标识符

    @JsonProperty("MsgTime")
    private String msgTime; // 发送接收消息时间

    @JsonProperty("Msg")
    private List<Object> msg; // 消息体

    @JsonProperty("start_time")
    private String startTime; // 车流量统计开始时间

    @JsonProperty("end_time")
    private String endTime; // 车流量统计结束时间

    @JsonProperty("IntersectionID")
    private String intersectionId; // 路口id

    @JsonProperty("Duration")
    private Integer duration;  // 信息收集的时长 second

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMsgSeq() {
        return msgSeq;
    }

    public void setMsgSeq(String msgSeq) {
        this.msgSeq = msgSeq;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public List<Object> getMsg() {
        return msg;
    }

    public void setMsg(List<Object> msg) {
        this.msg = msg;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }
}

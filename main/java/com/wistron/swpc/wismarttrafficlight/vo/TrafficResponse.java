package com.wistron.swpc.wismarttrafficlight.vo;

import com.wistron.swpc.wismarttrafficlight.util.DateUtil;

public class TrafficResponse {

    private Boolean success;
    private Long timestamp;
    private Object data;

    private TrafficResponse() {

    }

    public static TrafficResponse ok() {
        TrafficResponse response = new TrafficResponse();
        response.setSuccess(true);
        response.setTimestamp(DateUtil.getUtcMillis());
        return response;
    }

    public static TrafficResponse error() {
        TrafficResponse response = new TrafficResponse();
        response.setSuccess(false);
        response.setTimestamp(DateUtil.getUtcMillis());
        return response;
    }

    public TrafficResponse setResult(Object data) {
        this.setData(data);
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

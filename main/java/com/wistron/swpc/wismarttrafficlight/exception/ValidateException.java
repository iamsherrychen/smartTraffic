package com.wistron.swpc.wismarttrafficlight.exception;

public class ValidateException extends RuntimeException {

    private String msg;

    public ValidateException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

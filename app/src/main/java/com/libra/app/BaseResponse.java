package com.libra.app;

/**
 * Created by huyg on 2018/9/28.
 */

public class BaseResponse {
    private int code;
    private String msg;
    private String interval;

    public boolean isSuccess() {
        if (200 == code) {
            return true;
        }
        return false;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

}

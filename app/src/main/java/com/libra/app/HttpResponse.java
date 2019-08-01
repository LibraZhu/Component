package com.libra.app;

/**
 * Created by huyg on 2018/9/28.
 */

public class HttpResponse<T> extends BaseResponse {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.dejionline.base.model.response;


import lombok.Getter;

/**
 * 带对象的接口返回对象
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public class GenericResponse<T> extends BaseResponse {

    @Getter
    private T body;

    public GenericResponse(int code) {
        super(code);
    }

    public GenericResponse(int code, T body) {
        super(code);
        this.body = body;
    }
}

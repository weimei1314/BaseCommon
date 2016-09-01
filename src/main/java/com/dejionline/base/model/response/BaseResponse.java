package com.dejionline.base.model.response;

import lombok.Data;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/5.
 */
@Data
public class BaseResponse {

    //接口返回编码
    private int code;

    public BaseResponse(int code) {
        this.code = code;
    }
}

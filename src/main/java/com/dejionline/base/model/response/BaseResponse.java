package com.dejionline.base.model.response;

import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.commons.utils.GsonUtils;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/5.
 */
public class BaseResponse {

    //接口返回编码
    private int code;

    public BaseResponse(int code) {
        this.code = code;
    }

    public BaseResponse(ResponseCodeEnums enums) {
        this.code = enums.getCode();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}

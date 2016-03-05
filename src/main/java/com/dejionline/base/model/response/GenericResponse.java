package com.dejionline.base.model.response;


import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.commons.utils.GsonUtils;

/**
 * 带对象的接口返回对象
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public class GenericResponse<T> extends BaseResponse {

    private T body;

    public GenericResponse(int code) {
        super(code);
    }

    public GenericResponse(ResponseCodeEnums enums) {
        super(enums);
    }

    public GenericResponse(ResponseCodeEnums enums, T body) {
        super(enums);
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}

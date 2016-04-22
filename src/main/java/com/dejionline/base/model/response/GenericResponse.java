package com.dejionline.base.model.response;


import com.dejionline.base.commons.enums.ResponseCodeEnums;
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

    public GenericResponse(ResponseCodeEnums enums) {
        super(enums);
    }

    public GenericResponse(ResponseCodeEnums enums, T body) {
        super(enums);
        this.body = body;
    }
}

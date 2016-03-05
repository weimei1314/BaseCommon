package com.dejionline.base.exception;

import com.dejionline.base.commons.enums.ResponseCodeEnums;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/5.
 */
public class ServiceException extends RuntimeException {

    private int code;

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(ResponseCodeEnums enums) {
        super(enums.getMessage());
        this.code = enums.getCode();
    }

    public int getCode() {
        return code;
    }

    // 重写错误堆栈方法，减少性能损耗
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

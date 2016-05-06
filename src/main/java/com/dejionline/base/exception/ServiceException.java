package com.dejionline.base.exception;

import com.dejionline.base.commons.enums.ResponseCodeEnums;
import lombok.Getter;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/5.
 */
public class ServiceException extends RuntimeException {

    @Getter
    private int code;

    public ServiceException(int code) {
        super();
        this.code = code;
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(ResponseCodeEnums enums) {
        super(enums.getMessage());
        this.code = enums.getCode();
    }

    // 重写错误堆栈方法，减少性能损耗
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

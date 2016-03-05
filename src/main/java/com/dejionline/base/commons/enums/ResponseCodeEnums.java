package com.dejionline.base.commons.enums;

/**
 * 接口统一返回编码枚举类
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public enum ResponseCodeEnums {

    SUCCESS(-1, "接口调用成功"),

    RUNTIME_ERROR(-2, "程序运行时异常"),

    PARAMS_ERROR(-3, "参数错误"),

    NO_DATA(-7, "无数据"),

    PARAMS_EMPTY(-8, "参数为空"),

    DATABASE_ERROR(-22, "操作数据库失败"),

    CACHE_ERROR(-23, "操作缓存失败"),

    OTHER_SERVICE_ERROR(-202, "调用其它服务接口异常"),

    NO_PERMISSION(99, "没有权限"),

    //锁未释放
    LOCK_HAS_BEEN_ACQUIRED(203, "分布式锁未释放");

    // 接口返回编码
    private int code;

    // 接口返回编码描述
    private String message;

    ResponseCodeEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

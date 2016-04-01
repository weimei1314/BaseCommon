package com.dejionline.base.commons.enums;

/**
 * 接口统一返回编码枚举类
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public enum ResponseCodeEnums {

    //调用成功
    SUCCESS(-1, "接口调用成功"),

    RUNTIME_ERROR(-2, "程序运行时异常"),

    PARAMS_ERROR(-3, "参数错误"),

    NO_DATA(-7, "无数据"),

    PARAMS_EMPTY(-8, "参数为空"),

    //插入数据库失败
    DATABASE_ERROR(-22, "数据库操作失败"),

    //操作购物车缓存失败
    CACHE_ERROR(-23, "更新缓存失败"),

    //调用其它服务异常
    OTHER_SERVICE_ERROR(-202, "调用其它服务接口异常"),

    //调用其它服务超时
    OTHER_SERVICE_TIMEOUT(-203, "调用其它服务接口超时"),

    ORDER_QUERY_NO_DATA(-205, "订单查询无数据"),

    //没有权限
    NO_PERMISSION(99, "没有权限"),

    //创建订单时，有商品无法购买
    EXISTS_CAN_NOT_BUY_ITEMS(200, "存在无法购买的商品，无法创建订单"),

    //创建订单时，订单信息有误
    ORDER_INFO_NOT_MATCH(201, "订单信息有误，无法创建订单"),

    //购买商品超过限购数量
    OVER_ITEM_LIMIT_QUANTITY(202, "超过商品限购数量"),

    //锁未释放
    LOCK_HAS_BEEN_ACQUIRED(203, "分布式锁未释放"),

    //该订单状态无法进行此操作
    ORDER_STATUS_ERROR(204, "该订单状态无法进行此操作"),

    REQUEST_PAY_ERROR(205, "创建订单成功,请求支付失败"),

    LOCK_TIME_OUT_AND_AUTO_RELEASE(206, "分布式锁内业务执行超时，自动释放"),

    //支付流水不存在
    PAY_INFO_NOT_FOUND_BY_PAY_SERIAL(-300, "根据支付流水无法查询到订单"),

    //支付信息与支付方返回信息不一致
    PAY_INFO_NOT_MATCH(-301, "支付信息与支付方返回信息不一致"),

    RSA_SIGN_ERROR(-302, "RSA加密错误"),

    ILLEGAL_ALIPAY_CALLBACK_PARAM(-303, "支付宝异步通知参数错误"),

    ALIPAY_CALLBACK_RESPONSE_TXT_FALSE(-304, "支付宝异步通知responseTxt错误"),

    ALIPAY_CALLBACK_SIGN_ERROR(-305, "支付宝异步通知签名错误"),

    VERIFY_SIGN_ERROR(-306, "验签错误"),

    GET_UNIONPAY_TN_ERROR(-307, "获取银联TN号失败");

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

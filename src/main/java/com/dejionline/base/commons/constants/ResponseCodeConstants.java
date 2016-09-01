package com.dejionline.base.commons.constants;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/5/6.
 */
public class ResponseCodeConstants {

    //接口调用成功
    public static final int SUCCESS = -1;

    //程序运行时异常
    public static final int RUNTIME_ERROR = -2;

    //参数错误
    public static final int PARAMS_ERROR = -3;

    //无数据
    public static final int NO_DATA = -7;

    //参数为空
    public static final int PARAMS_EMPTY = -8;

    //数据库操作失败
    public static final int DATABASE_ERROR = -22;

    //更新缓存失败
    public static final int CACHE_ERROR = -23;

    //没有权限
    public static final int NO_PERMISSION = 99;

    //http请求返回结果错误
    public static final int HTTP_RESPONSE_ERROR = -201;

    //调用其它服务接口异常
    public static final int OTHER_SERVICE_ERROR = -202;

    //调用其它服务接口超时
    public static final int OTHER_SERVICE_TIMEOUT = -203;

    //httpclient从连接池获取连接超时
    public static final int GET_HTTPCLIENT_POOL_TIMEOUT = -204;

//    ORDER_QUERY_NO_DATA(-205, "订单查询无数据"),
//    PAY_INFO_NOT_FOUND_BY_PAY_SERIAL(-300, "根据支付流水无法查询到订单"),
//    PAY_INFO_NOT_MATCH(-301, "支付信息与支付方返回信息不一致"),
//    RSA_SIGN_ERROR(-302, "RSA加密错误"),
//    ILLEGAL_ALIPAY_CALLBACK_PARAM(-303, "支付宝异步通知参数错误"),
//    ALIPAY_CALLBACK_RESPONSE_TXT_FALSE(-304, "支付宝异步通知responseTxt错误"),
//    ALIPAY_CALLBACK_SIGN_ERROR(-305, "支付宝异步通知签名错误"),
//    VERIFY_SIGN_ERROR(-306, "验签错误"),
//    GET_UNIONPAY_TN_ERROR(-307, "获取银联TN号失败"),
//    EXISTS_CAN_NOT_BUY_ITEMS(200, "存在无法购买的商品，无法创建订单"),
//    ORDER_INFO_NOT_MATCH(201, "订单信息有误，无法创建订单"),
//    OVER_ITEM_LIMIT_QUANTITY(202, "超过商品限购数量"),
//    LOCK_HAS_BEEN_ACQUIRED(203, "分布式锁未释放"),
//    ORDER_STATUS_ERROR(204, "该订单状态无法进行此操作"),
//    REQUEST_PAY_ERROR(205, "创建订单成功,请求支付失败"),
//    LOCK_TIME_OUT_AND_AUTO_RELEASE(206, "分布式锁内业务执行超时，自动释放"),
//    PAY_EXPRIE(207, "订单支付过期时间已到，请重新下单"),
//    ORDER_ITEM_IS_COMMENTED(208, "该订单商品已评论");
}

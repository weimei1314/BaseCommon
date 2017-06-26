package com.dejionline.base.commons.utils;

import com.dejionline.base.commons.constants.ResponseCodeConstants;
import com.dejionline.base.exception.ServiceException;
import com.dejionline.base.model.response.BaseResponse;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

/**
 * 断言工具类
 * Created by ShengyangKong
 * on 2016/1/8.
 */
public class AssertUtils {

    private AssertUtils() {
    }

    public static void isNotBlank(String str, int code) {
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(code);
        }
    }

    public static void isNotNull(Object obj, int code) {
        Optional.ofNullable(obj).orElseThrow(() -> new ServiceException(code));
    }

    public static Object notNullObj(Object obj, int code) {
        return Optional.ofNullable(obj).orElseThrow(() -> new ServiceException(code));
    }

    public static void isTrue(boolean flag, int code) {
        if (!flag) {
            throw new ServiceException(code);
        }
    }

    public static void isNotEmpty(Collection<?> collection, int code) {
        if (collection == null || collection.isEmpty()) {
            throw new ServiceException(code);
        }
    }

    public static void isEquals(double d1, double d2, int code) {
        if (Math.abs(d1 - d2) >= 0.01) {
            throw new ServiceException(code);
        }
    }

    public static void isEquals(BigDecimal d1, BigDecimal d2, int code) {
        if (d1.subtract(d2).abs().compareTo(BigDecimal.valueOf(0.001)) != -1) {
            throw new ServiceException(code);
        }
    }

    public static void isSuccess(BaseResponse response) {
        if (ResponseCodeConstants.SUCCESS != response.getCode()) {
            throw new ServiceException(response.getCode(), "response code:" + response.getCode());
        }
    }

    public static void isSuccess(BaseResponse response, int code) {
        if (ResponseCodeConstants.SUCCESS != response.getCode()) {
            throw new ServiceException(code, "response code:" + response.getCode());
        }
    }
}

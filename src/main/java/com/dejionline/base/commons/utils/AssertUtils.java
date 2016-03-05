package com.dejionline.base.commons.utils;

import com.dejionline.base.commons.constants.BaseConstants;
import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.exception.ServiceException;
import com.dejionline.base.model.response.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 断言工具类
 * Created by ShengyangKong
 * on 2016/1/8.
 */
public class AssertUtils {

    private AssertUtils() {
    }

    public static void isNotBlank(String str, ResponseCodeEnums enums) {

        if (StringUtils.isBlank(str)) {

            throw new ServiceException(enums);
        }
    }

    public static void isNotNull(Object obj, ResponseCodeEnums enums) {

        if (null == obj) {

            throw new ServiceException(enums);
        }
    }

    public static void isTrue(boolean flag, ResponseCodeEnums enums) {

        if (!flag) {

            throw new ServiceException(enums);
        }
    }

    public static void isNotEmpty(Collection<?> collection, ResponseCodeEnums enums) {

        if (CollectionUtils.isEmpty(collection)) {

            throw new ServiceException(enums);
        }
    }

    public static void isNotNull(long l, ResponseCodeEnums enums) {

        if (BaseConstants.LONG_NULL_VALUE == l) {

            throw new ServiceException(enums);
        }
    }

    public static void isEquals(double d1, double d2, ResponseCodeEnums enums) {

        if (Math.abs(d1 - d2) >= 0.01) {

            throw new ServiceException(enums);
        }
    }

    public static void isSuccess(BaseResponse response) {

        if (ResponseCodeEnums.SUCCESS.getCode() != response.getCode()) {

            throw new ServiceException(response.getCode()
                    , ResponseCodeEnums.OTHER_SERVICE_ERROR.getMessage());
        }
    }

    public static void isSuccess(BaseResponse response, ResponseCodeEnums enums) {
        if (ResponseCodeEnums.SUCCESS.getCode() != response.getCode()) {
            throw new ServiceException(enums);
        }
    }
}

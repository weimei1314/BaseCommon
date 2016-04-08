package com.dejionline.base.interceptor;


import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.commons.utils.GsonUtils;
import com.dejionline.base.exception.ServiceException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/4.
 */
public class ControllerInterceptor implements MethodInterceptor {

    public static final Logger LOGGER = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result;

        Thread.currentThread().setName(DigestUtils.md5Hex(UUID.randomUUID().toString()));

        String methodName = invocation.getThis().getClass().getSimpleName() + "." + invocation.getMethod().getName();

        LOGGER.info("request|" + methodName + "|params:" + GsonUtils.toJson(invocation.getArguments()));

        try {

            result = invocation.proceed();

            LOGGER.info("response|" + methodName + "|cost:" + (System.currentTimeMillis() - startTime));

        } catch (ServiceException e) {

            result = invocation.getMethod().getReturnType().getConstructor(int.class).newInstance(e.getCode());

            LOGGER.info("serviceException|" + methodName + "|code:" + e.getCode() + "|msg:" + e.getMessage());

        } catch (Exception e) {

            result = invocation.getMethod().getReturnType().getConstructor(ResponseCodeEnums.class)
                    .newInstance(ResponseCodeEnums.RUNTIME_ERROR);

            LOGGER.error("otherException|" + methodName + "|msg:" + e.getMessage(), e);
        }

        return result;
    }
}

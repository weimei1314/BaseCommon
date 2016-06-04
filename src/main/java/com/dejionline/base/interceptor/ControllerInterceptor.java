package com.dejionline.base.interceptor;


import com.dejionline.base.annotation.DataSource;
import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.commons.utils.GsonUtils;
import com.dejionline.base.datasources.DynamicDataSourceHolder;
import com.dejionline.base.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/4.
 */
@Slf4j
public class ControllerInterceptor implements MethodInterceptor {

    private static final int LOG_LENGTH = 350;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result;

        Thread.currentThread().setName(DigestUtils.md5Hex(UUID.randomUUID().toString()));

        Method method = invocation.getMethod();

        String methodName = invocation.getThis().getClass().getSimpleName() + "." + method.getName();

        String params = GsonUtils.toJson(invocation.getArguments());

        log.info("request|" + methodName + "|params:" +
                (params.length() > LOG_LENGTH ? params.substring(0, LOG_LENGTH) : params));

        try {

            if (method.isAnnotationPresent(DataSource.class)) {

                DataSource data = method.getAnnotation(DataSource.class);

                DynamicDataSourceHolder.putDataSource(data.value());
            }

            result = invocation.proceed();

            String resultStr = GsonUtils.toJson(result);

            log.info("response|" + methodName + "|cost:" + (System.currentTimeMillis() - startTime)
                    + "|response:{}", resultStr.length() > LOG_LENGTH ? resultStr.substring(0, LOG_LENGTH) : resultStr);

        } catch (ServiceException e) {

            result = invocation.getMethod().getReturnType().getConstructor(int.class).newInstance(e.getCode());

            log.info("serviceException|" + methodName + "|code:" + e.getCode());

        } catch (Exception e) {

            result = invocation.getMethod().getReturnType().getConstructor(ResponseCodeEnums.class)
                    .newInstance(ResponseCodeEnums.RUNTIME_ERROR);

            log.error("otherException|" + methodName, e);
        }

        return result;
    }
}

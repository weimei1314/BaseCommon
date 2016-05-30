package com.dejionline.base.interceptor;


import com.dejionline.base.annotation.DataSource;
import com.dejionline.base.commons.enums.ResponseCodeEnums;
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

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result;

        Thread.currentThread().setName(DigestUtils.md5Hex(UUID.randomUUID().toString()));

        Method method = invocation.getMethod();

        String methodName = invocation.getThis().getClass().getSimpleName() + "." + method.getName();

        log.info("request|" + methodName);

        try {

            if (method.isAnnotationPresent(DataSource.class)) {

                DataSource data = method.getAnnotation(DataSource.class);

                DynamicDataSourceHolder.putDataSource(data.value());
            }

            result = invocation.proceed();

            log.info("response|" + methodName + "|cost:" + (System.currentTimeMillis() - startTime));

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

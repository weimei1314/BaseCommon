package com.dejionline.base.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/5.
 */
public class ThreadNumConverter {

    public ThreadNumConverter() {
    }

    public String convert(ILoggingEvent event) {
        return DigestUtils.md5Hex("" + Thread.currentThread().getId());
    }

}

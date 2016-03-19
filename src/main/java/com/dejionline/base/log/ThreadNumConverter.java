package com.dejionline.base.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 重写logback方法，将线程id md5加密打出，便于定位日志
 * Created by ShengyangKong
 * on 2016/1/21.
 */
public class ThreadNumConverter extends ClassicConverter {

    /**
     * 当需要显示线程ID的时候，返回当前调用线程的ID
     */
    @Override
    public String convert(ILoggingEvent event) {

        return DigestUtils.md5Hex("" + Thread.currentThread().getId());
    }
}

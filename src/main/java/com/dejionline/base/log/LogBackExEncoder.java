package com.dejionline.base.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.IOException;

/**
 * 重写logback方法，将线程id md5加密打出，便于定位日志
 * Created by ShengyangKong
 * on 2016/1/21.
 */
public class LogBackExEncoder extends PatternLayoutEncoder {

    static {

        PatternLayout.defaultConverterMap.put("T", ThreadNumConverter.class.getName());
    }

    @Override
    public void doEncode(ILoggingEvent event) throws IOException {

        super.doEncode(event);
    }
}

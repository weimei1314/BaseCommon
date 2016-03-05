package com.dejionline.base.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.IOException;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/3/5.
 */
public class LogBackExEncoder extends PatternLayoutEncoder {

    public LogBackExEncoder() {
    }

    public void doEncode(ILoggingEvent event) throws IOException {

        super.doEncode(event);
    }

    static {

        PatternLayout.defaultConverterMap.put("T", ThreadNumConverter.class.getName());

        PatternLayout.defaultConverterMap.put("threadNum", ThreadNumConverter.class.getName());
    }
}

package com.dejionline.base.commons.utils;


import com.dejionline.base.exception.ServiceException;

import java.io.IOException;
import java.util.Properties;

/**
 * 环境动态配置工具类
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public class ConfigUtils {

    private static Properties p = null;

    private static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    // 工具类不允许实例化
    private ConfigUtils() {
    }

    // 获取配置文件
    public static Properties getProperties() {

        if (p == null) {

            p = new Properties();

            try {

                p.load(ConfigUtils.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE_NAME));

            } catch (IOException e) {

                throw new ServiceException(e.getMessage());
            }
        }

        return p;
    }

    //根据key获取配置文件中的值
    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }
}

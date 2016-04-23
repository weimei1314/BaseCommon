package com.dejionline.base.commons.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 环境动态配置工具类
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public class ConfigUtils {

    private static Config conf;

    // 工具类不允许实例化
    private ConfigUtils() {
    }

    public static Config conf() {

        if (conf == null) {

            conf = ConfigFactory.load();
        }

        return conf;
    }
}

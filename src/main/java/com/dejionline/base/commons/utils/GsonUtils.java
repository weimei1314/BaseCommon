package com.dejionline.base.commons.utils;

import com.google.gson.Gson;

/**
 * json工具类
 * Created by ShengyangKong
 * on 2015/12/28.
 */
public class GsonUtils {

    private static final Gson gson = new Gson();

    // 工具类不允许实例化
    private GsonUtils() {
    }

    public static Gson getGson() {
        return gson;
    }
}

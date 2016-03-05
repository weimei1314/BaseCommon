package com.dejionline.base.commons.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

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

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}

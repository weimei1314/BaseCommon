package com.dejionline.base.datasources;

/**
 * functional description
 * Created by Sandy
 * on 2016/5/24.
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static void putDataSource(String name) {
        holder.set(name);
    }

    public static String getDataSource() {
        return holder.get();
    }
}

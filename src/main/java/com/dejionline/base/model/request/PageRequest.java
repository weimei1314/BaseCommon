package com.dejionline.base.model.request;


import lombok.Data;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/1/27.
 */
@Data
public class PageRequest {

    private static final int LIMIT_MAX = 100;

    private static final int MIN = 0;

    //当前页码
    private int current;

    //每页数量
    private int limit;

    //sql偏移量
    private Integer offset;

    public void setLimit(int limit) {
        this.limit = Math.max(MIN, Math.min(LIMIT_MAX, limit));
    }

    public void setCurrent(int current) {
        this.current = Math.max(MIN, current);
    }
}
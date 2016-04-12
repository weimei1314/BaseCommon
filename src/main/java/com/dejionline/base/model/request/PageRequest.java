package com.dejionline.base.model.request;


import com.dejionline.base.commons.constants.BaseConstants;
import com.dejionline.base.commons.utils.GsonUtils;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/1/27.
 */
public class PageRequest {

    private static final int LIMIT_MAX = 100;

    //当前页码
    private int current = BaseConstants.INT_NULL_VALUE;

    //每页数量
    private int limit = BaseConstants.INT_NULL_VALUE;

    //sql偏移量
    private int offset = BaseConstants.INT_NULL_VALUE;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = Math.min(limit, LIMIT_MAX);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}

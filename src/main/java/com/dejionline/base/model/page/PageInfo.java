package com.dejionline.base.model.page;

import com.dejionline.base.commons.utils.GsonUtils;

import java.util.List;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/4/14.
 */
public class PageInfo<T> {

    private Long count;

    private List<T> body;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getBody() {
        return body;
    }

    public void setBody(List<T> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}

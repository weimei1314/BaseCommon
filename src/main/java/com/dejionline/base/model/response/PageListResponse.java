package com.dejionline.base.model.response;


import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.commons.utils.GsonUtils;
import com.dejionline.base.model.page.PageInfo;

import java.util.List;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/2/19.
 */
public class PageListResponse<T> extends BaseResponse {

    private Long count;

    private List<T> body;

    public PageListResponse(int code) {
        super(code);
    }

    public PageListResponse(ResponseCodeEnums enums) {
        super(enums);
    }

    public PageListResponse(ResponseCodeEnums enums, List<T> body) {
        super(enums);
        this.body = body;
    }

    public PageListResponse(ResponseCodeEnums enums, List<T> body, Long count) {
        super(enums);
        this.body = body;
        this.count = count;
    }

    public PageListResponse(ResponseCodeEnums enums, PageInfo<T> pageInfo) {
        super(enums);
        this.body = pageInfo.getBody();
        this.count = pageInfo.getCount();
    }

    public Long getCount() {
        return count;
    }

    public List<T> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}

package com.dejionline.base.model.response;


import com.dejionline.base.model.page.PageInfo;
import lombok.Getter;

import java.util.List;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/2/19.
 */
public class PageListResponse<T> extends BaseResponse {

    @Getter
    private Long count;

    @Getter
    private List<T> body;

    public PageListResponse(int code) {
        super(code);
    }

    public PageListResponse(int code, List<T> body) {
        super(code);
        this.body = body;
    }

    public PageListResponse(int code, List<T> body, Long count) {
        super(code);
        this.body = body;
        this.count = count;
    }

    public PageListResponse(int code, PageInfo<T> pageInfo) {
        super(code);
        this.body = pageInfo.getBody();
        this.count = pageInfo.getCount();
    }
}

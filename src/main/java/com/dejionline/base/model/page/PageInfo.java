package com.dejionline.base.model.page;

import lombok.Data;

import java.util.List;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/4/14.
 */
@Data
public class PageInfo<T> {

    private Long count;

    private List<T> body;
}

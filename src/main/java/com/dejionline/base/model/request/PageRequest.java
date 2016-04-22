package com.dejionline.base.model.request;


import com.dejionline.base.commons.constants.BaseConstants;
import lombok.Data;

/**
 * functional description
 * Created by ShengyangKong
 * on 2016/1/27.
 */
@Data
public class PageRequest {

    private static final int LIMIT_MAX = 100;

    //当前页码
    private int current = BaseConstants.INT_NULL_VALUE;

    //每页数量
    private int limit = BaseConstants.INT_NULL_VALUE;

    //sql偏移量
    private Integer offset;
}

package com.dejionline.base.datasources;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * functional description
 * Created by Sandy
 * on 2016/5/24.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        return DynamicDataSourceHolder.getDataSource();
    }
}

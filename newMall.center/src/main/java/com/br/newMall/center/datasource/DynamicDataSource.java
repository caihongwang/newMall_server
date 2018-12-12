package com.br.newMall.center.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.logging.Logger;

/**
 * Created by caihongwang on 17/3/16.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    public Logger getParentLogger() {
        return super.getParentLogger();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDbType();
    }
}

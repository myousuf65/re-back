package com.hkmci.csdkms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PoolSizeChecker {
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }
}

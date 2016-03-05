package com.dejionline.base.commons.utils;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;

/**
 * 缓存分布式锁工具类
 * Created by ShengyangKong
 * on 1/12/16.
 */
public class CacheLockUtils {

    private static final int CHANNEL = 6;

    private static RedissonClient redissonClient;

    protected CacheLockUtils() {
    }

    public static void initCacheLock() {

        if (redissonClient == null) {

            Config config = new Config();

            config.useSingleServer().setAddress(ConfigUtils.getProperty("cache.lock.host"))
                    .setDatabase(CHANNEL);

            redissonClient = Redisson.create(config);
        }
    }

    public static RLock createLock(String name) {

        initCacheLock();

        return redissonClient.getLock(name);
    }
}

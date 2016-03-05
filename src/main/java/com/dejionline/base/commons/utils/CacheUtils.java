package com.dejionline.base.commons.utils;

import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.exception.ServiceException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 缓存操作工具类
 * Created by ShengyangKong
 * on 2016/1/8.
 */
public class CacheUtils {

    // 工具类不允许实例化
    protected CacheUtils() {
    }

    private static JedisPool jedisPool = null;

    public static void initCache() {

        if (jedisPool == null) {

            GenericObjectPoolConfig config = new JedisPoolConfig();

            config.setTestOnBorrow(true);

            config.setMaxTotal(500);

            config.setMaxWaitMillis(15000);

            config.setMaxIdle(20);

            jedisPool = new JedisPool(config, ConfigUtils.getProperty("cache.server.ip")
                    , Integer.parseInt(ConfigUtils.getProperty("cache.server.port")));
        }
    }

    public static boolean setOrUpdateCache(int channel, String key, String value) {

        initCache();

        Jedis jedis = jedisPool.getResource();

        jedis.select(channel);

        try {

            jedis.set(key, value);

        } catch (Exception e) {

            throw new ServiceException(ResponseCodeEnums.CACHE_ERROR);

        } finally {

            returnResource(jedis, jedisPool);
        }

        return true;
    }

    public static String getCache(int channel, String key) {

        initCache();

        String value = null;

        Jedis jedis = jedisPool.getResource();

        jedis.select(channel);

        try {

            value = jedis.get(key);

        } catch (Exception e) {

            throw new ServiceException(ResponseCodeEnums.CACHE_ERROR);

        } finally {

            returnResource(jedis, jedisPool);
        }

        return value;
    }

    public static boolean delCache(int channel, String key) {

        initCache();

        Jedis jedis = jedisPool.getResource();

        jedis.select(channel);

        try {

            jedis.del(key);

        } catch (Exception e) {

            throw new ServiceException(ResponseCodeEnums.CACHE_ERROR);

        } finally {

            returnResource(jedis, jedisPool);
        }

        return true;
    }

    private static void returnResource(Jedis jedis, JedisPool jedisPool) {

        if (jedis != null) {

            jedisPool.returnResourceObject(jedis);
        }
    }
}

package com.example.lib.service;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

import static ch.qos.logback.core.spi.ComponentTracker.DEFAULT_TIMEOUT;

public class CacheManager implements CacheClient {
    private final JedisPool pool;
    public CacheManager(String host, int port, boolean failOnError, JedisPoolConfig poolConfig) {
        pool = new JedisPool(poolConfig, host, port, DEFAULT_TIMEOUT);
    }


    @Override
    public void set(String key, Object value) {

    }

    @Override
    public Object get(String key) {
       try(Jedis jedis = pool.getResource()) {
           return jedis.get(key);
       }
    }

    @Override
    public void delete(String key) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        }
    }

    @Override
    public void deleteAll(List<String> keys) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(keys.toArray(new String[0]));
        }
    }

    @Override
    public void shutdown() {
        pool.close();
    }
}

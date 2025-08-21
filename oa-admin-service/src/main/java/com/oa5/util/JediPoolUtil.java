package com.oa5.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;

@Component
public class JediPoolUtil {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password:}")
    private String password;

    @Value("${redis.maxTotal}")
    private int maxTotal;

    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Value("${redis.maxWait}")
    private long maxWait;

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    private JedisPool jedisPool;

    @PostConstruct
    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWait(Duration.ofMillis(maxWait));
        config.setTestOnBorrow(testOnBorrow);

        if (password == null || password.isEmpty()) {
            jedisPool = new JedisPool(config, host, port);
        } else {
            jedisPool = new JedisPool(config, host, port, 2000, password);
        }
    }

    /**
     * 正确获取 Jedis：每次获取，使用后一定要 close
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @PreDestroy
    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}

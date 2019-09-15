package com.atguigu.gmall.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    //连接池
    private JedisPool jedisPool = null;
    //初始化连接池
    public void initJedisPool(String host, int port, int database){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        //从服务器性能，从用户量，集群
        //最大连接数
        jedisPoolConfig.setMaxTotal(100);
        //链接毫秒数
        jedisPoolConfig.setMaxWaitMillis(10*1000);
        //最少剩余数
        jedisPoolConfig.setMinIdle(10);
        //如果到最大数，设置等待
        jedisPoolConfig.setBlockWhenExhausted(true);
        //在获取连接时，检查是否有效
        jedisPoolConfig.setTestOnBorrow(true);

        jedisPool = new JedisPool(jedisPoolConfig,host,port,20*1000);

    }

    //从jedispool中获取jedis
    public Jedis getJedis(){
        Jedis resource = jedisPool.getResource();
        return resource;
    }
}

package com.atguigu.gmall.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration //当作xml
public class RedisConfig {
    //文件配置放在gmall-manage，使用@value读取配置文件
    //:disabled 表示配置文件中不存在host，则host默认为disabled
    @Value("${spring.redis.host:disabled}")
    private String host;

    @Value("${spring.redis.port:0}")
    private int port;

    @Value("${spring.redis.database:0}")
    private int database;

    @Bean  //spring管理
    //将host，port，database 给 RedisUtil.initJedisPool();
    public RedisUtil test(){
        if(StringUtils.equals("disabled",host)){
            return null;
        }

        //真正去初始化的操作
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.initJedisPool(host,port,database);
        return redisUtil;

    }

}


package com.example.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置类
 * @author 孙灵达
 * @date 2020-12-30
 */
@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 节点
     */
    @Value("${spring.redis.cluster.nodes}")
    private String redisNodes;

    /**
     * 连接池最大连接数（使用负值表示没有限制）
     */
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制）
     */
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait;

    /**
     * 连接池中的最大空闲连接
     */
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    /**
     * 连接池中的最小空闲连接
     */
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    /**
     * 连接超时时间（毫秒）
     */
    @Value("${spring.redis.timeout}")
    private int timeOut;


    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration clusterConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(clusterConfig);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(createJedisPoolConfig());
        return new JedisConnectionFactory(clusterConfig);
    }


    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        String[] nodes = redisNodes.split(",");
        for (String node : nodes) {
            String[] host = node.split(":");
            RedisNode redis = new RedisNode(host[0], Integer.parseInt(host[1]));
            config.addClusterNode(redis);
        }
        return config;
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        // json转对象类，不设置默认的会将json转成HashMap
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    private JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 连接耗尽时是否阻塞, false报异常,true阻塞直到超时, 默认true
        config.setBlockWhenExhausted(false);
        // 是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(true);
        //最大空闲连接数, 默认8个
        config.setMaxIdle(maxIdle);
        //最大连接数, 默认8个
        config.setMaxTotal(maxActive);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(maxWait);
        //最小空闲连接数, 默认0
        config.setMinIdle(minIdle);
        //超时时间
        config.setEvictorShutdownTimeoutMillis(timeOut);
        return config;
    }
}

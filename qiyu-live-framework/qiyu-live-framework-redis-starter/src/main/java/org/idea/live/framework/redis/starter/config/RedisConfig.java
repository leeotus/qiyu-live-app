package org.idea.live.framework.redis.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis自动配置类
 * 当类路径下存在RedisTemplate类时，自动加载此配置
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class RedisConfig {

    /**
     * 配置RedisTemplate Bean
     * @param redisConnectionFactory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 创建RedisTemplate实例
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置Redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 创建JSON序列化器
        IGenericJackson2JsonRedisSerializer valueSerializer = new IGenericJackson2JsonRedisSerializer();
        // 创建字符串序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置key的序列化器为字符串序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 设置value的序列化器为JSON序列化器
        redisTemplate.setValueSerializer(valueSerializer);
        // 设置hash key的序列化器为字符串序列化器
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 设置hash value的序列化器为JSON序列化器
        redisTemplate.setHashValueSerializer(valueSerializer);
        // 初始化RedisTemplate
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

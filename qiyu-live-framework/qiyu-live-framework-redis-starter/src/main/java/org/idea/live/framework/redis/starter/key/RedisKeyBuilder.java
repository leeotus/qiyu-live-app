package org.idea.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * Redis键构建器类
 * 用于构建Redis键的前缀和分隔符
 */
public class RedisKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;  // 应用名称，从配置文件中注入

    private static final String SPLIT_ITEM = ":";  // Redis键分隔符，冒号

    /**
     * 获取Redis键分隔符
     * @return 返回分隔符字符串
     */
    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    /**
     * 获取Redis键前缀
     * @return 返回应用名称加分隔符组成的字符串
     */
    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }
}

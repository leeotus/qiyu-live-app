package org.idea.live.framework.redis.starter.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * RedisKeyLoadMatch类实现了Condition接口，用于条件化加载RedisKeyBuilder对象
 * 该类主要负责检查应用名称是否匹配，并决定是否加载相应的RedisKeyBuilder
 */
public class RedisKeyLoadMatch implements Condition {
    // 创建一个日志记录器，用于记录类中的日志信息
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisKeyLoadMatch.class);

    // 定义一个常量字符串前缀，用于RedisKey的命名规范
    private static final String PREFIX = "qiyulive";

    /**
     * 实现Condition接口的matches方法，用于判断是否满足加载条件
     * @param context 条件上下文，可以获取环境信息等
     * @param metadata 带注解的类型的元数据信息
     * @return 如果条件满足返回true，否则返回false
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String appName = context.getEnvironment().getProperty("spring.application.name");
        if (appName == null) {
            LOGGER.error("没有匹配到应用名称，所以无法加载任何RedisKeyBuilder对象");
            return false;
        }
        boolean matchStatus = false;
        try {
            Field classNameField = metadata.getClass().getDeclaredField("className");
            classNameField.setAccessible(true);
            String keyBuilderName = (String) classNameField.get(metadata);
            List<String> splitList = Arrays.asList(keyBuilderName.split("\\."));
            String classSimplyName = PREFIX + splitList.get(splitList.size() - 1).toLowerCase();
            matchStatus = classSimplyName.contains(appName.replaceAll("-", ""));
            LOGGER.info("keyBuilderClass is {},matchStatus is {}", keyBuilderName, matchStatus);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}

package org.idea.live.user.provider.config;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.idea.live.common.interfaces.topic.UserProviderTopicNames;
import org.idea.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.idea.live.user.constants.CacheAsyncDeleteCode;
import org.idea.live.user.dto.UserCacheAsyncDeleteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;

import jakarta.annotation.Resource;

@Configuration
public class RocketMQConsumerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    /**
     * 初始化方法，在Bean属性设置完成后调用，用于初始化RocketMQ消费者
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建默认的RocketMQ推送消费者实例
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        // 禁用VIP通道，避免端口占用问题
        mqPushConsumer.setVipChannelEnabled(false);
        // 设置NameServer地址，用于获取Broker信息
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        // 设置消费者组名称，包含类名以区分不同消费者
        mqPushConsumer.setConsumerGroup(
                rocketMQConsumerProperties.getGroupName() + "_" + RocketMQConsumerConfig.class.getSimpleName());
        // 设置每次消费的消息最大数量为1，即单条消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(1);
        // 设置消费起始位置为从最早的消息开始消费
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅缓存异步删除主题，"*"表示订阅所有标签
        mqPushConsumer.subscribe(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC, "");
        // 设置消息监听器，实现消息消费逻辑
        mqPushConsumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 将消息体转换为JSON字符串
                String json = new String(msgs.get(0).getBody());
                // 将JSON字符串解析为用户缓存异步删除DTO对象
                UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = JSON.parseObject(json, UserCacheAsyncDeleteDTO.class);
                // 判断是否为用户信息删除消息
                if (CacheAsyncDeleteCode.USER_INFO_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                    // 从消息中提取用户ID
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    // 删除用户信息缓存
                    redisTemplate.delete(cacheKeyBuilder.buildUserInfoKey(userId));
                    // 记录删除用户信息缓存的日志
                    LOGGER.info("延迟删除用户信息缓存，userId is {}", userId);
                } else if (CacheAsyncDeleteCode.USER_TAG_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    redisTemplate.delete(cacheKeyBuilder.buildTagKey(userId));
                    LOGGER.info("延迟删除用户标签缓存，userId is {}", userId);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        mqPushConsumer.start();
        LOGGER.info("mq消费者启动成功,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }

}

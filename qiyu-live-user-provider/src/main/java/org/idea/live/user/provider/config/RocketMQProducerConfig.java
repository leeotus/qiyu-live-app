package org.idea.live.user.provider.config;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

@Configuration
public class RocketMQProducerConfig {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQProducerConfig.class);

    @Resource
    private RocketMQProducerProperties producerProperties;
    
    /**
     * 创建并配置RocketMQ生产者Bean
     * 
     * @return 配置完成的DefaultMQProducer实例
     * @note @Bean使Spring调用这个方法来创建对象
     */
    @Bean
    public MQProducer mqProducer() {
        // 创建线程池，用于异步发送消息
        // 核心线程数为CPU核心数*2，最大线程数为100
        // 空闲线程存活时间为30秒
        // 使用容量为2000的ArrayBlockingQueue作为工作队列
        ThreadPoolExecutor asyncThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 100,
                        30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                // 为每个线程设置名称，格式为"rocketmq-async-thread-"加随机数
                return new Thread(r, "rocketmq-async-thread-" + new Random().ints().toString());
            }
                });
        // 创建默认的MQ生产者实例
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setProducerGroup(producerProperties.getGroupName());
        // 设置NameServer地址
        defaultMQProducer.setNamesrvAddr(producerProperties.getNameSrv());
        // 设置同步发送失败时的重试次数
        defaultMQProducer.setRetryTimesWhenSendFailed(producerProperties.getRetryTimes());
        // 设置异步发送失败时的重试次数
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(producerProperties.getRetryTimes());
        
        // 设置当消息存储不成功时是否重试其他Broker
        // NOTE: 目前本地只搭载了一个braker
        defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
        // 设置异步发送线程池
        defaultMQProducer.setAsyncSenderExecutor(asyncThreadPool);

        try {
            // 启动生产者
            defaultMQProducer.start();
        } catch (MQClientException e) {
            // 打印异常堆栈信息
            return defaultMQProducer;
        }
        // 记录生产者启动成功的日志，包含NameServer地址
        LOGGER.info("mq生产者启动成功: namesrv is {}", producerProperties.getNameSrv());
        return defaultMQProducer;
    }
}

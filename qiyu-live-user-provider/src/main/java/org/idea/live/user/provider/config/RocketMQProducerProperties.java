package org.idea.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * RocketMQ生产者配置属性类
 * 用于配置RocketMQ生产者的相关参数
 */
@Data
@ConfigurationProperties(prefix = "qiyu.rmq.producer")
@Configuration
public class RocketMQProducerProperties {

    private String nameSrv;     // rocketmq的nameServer地址，用于集群节点发现
    private String groupName;   // 分组名称，用于消息发送和消费的分组标识
    private int retryTimes;     // 消息重发次数，消息发送失败后的重试次数
    private int sendTimeOut;    // 发送超时时间，单位毫秒，消息发送超时时间设置

    /**
     * 重写toString方法
     * @return 返回RocketMQ生产者配置属性的字符串表示
     */
    @Override
    public String toString() {
        return "RocketMQProducerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                ", retryTimes=" + retryTimes +
                ", sendTimeOut=" + sendTimeOut +
                '}';
    }
}

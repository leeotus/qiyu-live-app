package org.idea.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "qiyu.rmq.consumer")
public class RocketMQConsumerProperties {
    private String nameSrv;
    private String groupName;
    
    @Override
    public String toString() {
        return "RocketMQConsumerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}

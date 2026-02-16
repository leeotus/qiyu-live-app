package org.idea.live.msg.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "qiyu.sms.ccp")
@Configuration
public class ApplicationProperties {
    private String smsServerIp;
    private Integer port;
    private String accountSId;
    private String accountToken;
    private String appId;
    private String testPhone;

    @Override
    public String toString() {
        return "ApplicationProperties{" +
                "smsServerIp='" + smsServerIp + '\'' +
                ", port=" + port +
                ", accountSId='" + accountSId + '\'' +
                ", accountToken='" + accountToken + '\'' +
                ", testPhone='" + testPhone + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}

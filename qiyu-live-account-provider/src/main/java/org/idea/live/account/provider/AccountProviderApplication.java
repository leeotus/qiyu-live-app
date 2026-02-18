package org.idea.live.account.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class AccountProviderApplication {

    private static final Logger log = LoggerFactory.getLogger(AccountProviderApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AccountProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);

        new Thread(
                () -> {
                    synchronized (AccountProviderApplication.class) {
                        try {
                            AccountProviderApplication.class.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

        log.info("AccountProvider started");
    }
}

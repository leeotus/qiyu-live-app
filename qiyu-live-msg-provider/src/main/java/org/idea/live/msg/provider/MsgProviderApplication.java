package org.idea.live.msg.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.idea.live.msg.dto.MsgCheckDTO;
import org.idea.live.msg.enums.MsgSendResultEnum;
import org.idea.live.msg.provider.service.ISmsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class MsgProviderApplication  {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MsgProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);

        new Thread(
                () -> {
                    synchronized (MsgProviderApplication.class) {
                        try {
                            MsgProviderApplication.class.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

}

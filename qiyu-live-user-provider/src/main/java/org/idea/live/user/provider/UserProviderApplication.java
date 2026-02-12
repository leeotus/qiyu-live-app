package org.idea.live.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.idea.live.user.constants.UserTagsEnum;
import org.idea.live.user.provider.service.IUserTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import jakarta.annotation.Resource;

/**
 * Spring Boot应用程序主启动类
 * 使用@SpringBootApplication注解标记这是一个Spring Boot应用程序
 * 使用@EnableDubbo注解启用Dubbo功能
 * 使用@EnableDiscoveryClient注解启用服务发现功能
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication {
    private static final Logger log = LoggerFactory.getLogger(UserProviderApplication.class);

    /**
     * 应用程序主入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建SpringApplication实例，指定主启动类
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        // 设置应用程序类型为非Web应用
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        // 运行Spring应用程序
        springApplication.run(args);
        log.info("user-provider启动完成");

        new Thread(
                () -> {
                    synchronized (UserProviderApplication.class) {
                        try {
                            UserProviderApplication.class.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

    }
}

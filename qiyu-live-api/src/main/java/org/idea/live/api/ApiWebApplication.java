package org.idea.live.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Boot应用程序的主启动类
 * 使用@SpringBootApplication注解标记这是一个Spring Boot应用程序
 * 使用@EnableDiscoveryClient注解启用服务发现客户端功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiWebApplication {
    /**
     * 程序的主入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建SpringApplication实例，指定Appendable.class作为主配置类
        SpringApplication springApplication = new SpringApplication(ApiWebApplication.class);
        // 设置应用程序类型为Servlet类型
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        // 运行Spring应用程序，传入命令行参数
        springApplication.run(args);
    }
}

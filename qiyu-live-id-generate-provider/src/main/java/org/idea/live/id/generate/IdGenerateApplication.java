package org.idea.live.id.generate;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.idea.live.id.generate.service.IdGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGenerateApplication {
    private static final Logger log = LoggerFactory.getLogger(IdGenerateApplication.class);

    @Resource
    private IdGenerateService idGenerateService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
        log.info("id-generate-provider启动完成");

        // 防止该任务退出
        new Thread(
                () -> {
                    synchronized (IdGenerateApplication.class) {
                        try {
                            IdGenerateApplication.class.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    // 测试需要该类implements CommandLineRunner
    // @Override
    // public void run(String... args) throws Exception {
    //     for (int i = 0; i < 2000; ++i) {
    //         // Long id = idGenerateService.getSeqId(1);
    //         Long id = idGenerateService.getUnSeqId(2);
    //         System.out.println(id);
    //     }
    // }
}

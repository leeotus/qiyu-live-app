package org.idea.live.im.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.idea.live.im.core.server.common.ImMsgDecoder;
import org.idea.live.im.core.server.common.ImMsgEncoder;
import org.idea.live.im.core.server.handler.ImServerCoreHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ImCoreServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ImCoreServerApplication.class);

    private int port; // 指定监听的端口

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    // 基于netty去启动一个java进程, 绑定监听端口
    public void startApplication(int port) throws InterruptedException {
        setPort(port);
        // 处理accept事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        // 处理read & write事件
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                // 打印日志,方便观察
                logger.info("new channel");
                // 设计消息体

                // 增加编解码器
                channel.pipeline().addLast(new ImMsgDecoder()); // 客户端->服务端
                channel.pipeline().addLast(new ImMsgEncoder()); // 服务端->客户端
                // 设置netty处理handler
                channel.pipeline().addLast(new ImServerCoreHandler()); // 处理解码后的消息

            }
        });

        // 基于JVM的钩子函数去实现优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));

        ChannelFuture cha = bootstrap.bind(port).sync();
        logger.info("server started, listen port:" + port);
        // 这里会阻塞掉主线程, 实现长期服务器的效果
        cha.channel().closeFuture().sync();

    }

    public static void main(String[] args) throws InterruptedException {
        ImCoreServerApplication server = new ImCoreServerApplication();
        server.startApplication(9090);
    }

    // public static void main(String[] args) throws InterruptedException {
    // SpringApplication springApplication = new
    // SpringApplication(ImCoreServerApplication.class);
    // springApplication.setWebApplicationType(WebApplicationType.NONE);
    // springApplication.run(args);

    // new Thread(
    // () -> {
    // synchronized (ImCoreServerApplication.class) {
    // try {
    // ImCoreServerApplication.class.wait();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }).start();
    // }
}

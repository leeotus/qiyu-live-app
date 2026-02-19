package imclient;

import org.idea.live.im.core.server.common.ImMsg;
import org.idea.live.im.core.server.common.ImMsgDecoder;
import org.idea.live.im.core.server.common.ImMsgEncoder;
import org.idea.live.im.interfaces.ImMsgCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ImClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(ImClientApplication.class);

    private void startConnection(String addr, int port) throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                logger.info("ImClientApplication.initChannel");
                ch.pipeline().addLast((ChannelHandler) new ImMsgDecoder());
                ch.pipeline().addLast((ChannelHandler) new ImMsgEncoder());
                ch.pipeline().addLast((ChannelHandler) new ClientHandler());
            }

        });

        ChannelFuture channelFuture = bootstrap.connect(addr, port).sync();
        Channel channel = channelFuture.channel();
        for (int i = 0; i < 10; ++i) {
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), "login"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), "logout"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), "biz msg"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), "heartbeat"));
            Thread.sleep(3000);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        ImClientApplication client = new ImClientApplication();
        client.startConnection("localhost", 9090);
    }
}

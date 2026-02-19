package imclient;

import org.idea.live.im.core.server.common.ImMsg;
import org.slf4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ImMsg imMsg = (ImMsg) msg;

        // 简单输出服务器端传来的数据
        logger.info("client receive msg, code is {}, body is {}", imMsg.getCode(), new String(imMsg.getBody()));
    }

}

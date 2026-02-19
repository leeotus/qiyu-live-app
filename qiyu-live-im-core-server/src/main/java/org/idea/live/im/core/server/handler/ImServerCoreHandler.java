package org.idea.live.im.core.server.handler;

import org.idea.live.im.core.server.common.ImMsg;
import org.idea.live.im.core.server.handler.impl.ImHandlerFactoryImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ImServerCoreHandler extends SimpleChannelInboundHandler {

    private ImHandlerFactory imHandlerFactory = new ImHandlerFactoryImpl();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ImMsg)) {
            throw new IllegalArgumentException("error msg, msg is " + msg);
        }
        ImMsg imMsg = (ImMsg) msg;
        imHandlerFactory.doMsgHandler(ctx, imMsg);
        // 消息包
        // 1. 登录消息包: 登录token认证, channel和userId关联
        // 2. 登出消息包: 正常断开im连接的时候发送的
        // 3. 业务消息包: 最常用的消息类型, 例如我们的im发送数据, 或者接收数据的时候会用到
        // 4. 心跳消息包: 定时会给im发送, 汇报功能
    }

}

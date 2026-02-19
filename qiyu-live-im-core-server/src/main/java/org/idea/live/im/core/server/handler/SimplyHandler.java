package org.idea.live.im.core.server.handler;

import org.idea.live.im.core.server.common.ImMsg;

import io.netty.channel.ChannelHandlerContext;

public interface SimplyHandler {

    /**
     * 消息处理函数
     * 
     * @param ctx [in] 处理消息的上下文对象
     * @param msg [in] 需要处理的消息
     */
    void handler(ChannelHandlerContext ctx, ImMsg msg);
}

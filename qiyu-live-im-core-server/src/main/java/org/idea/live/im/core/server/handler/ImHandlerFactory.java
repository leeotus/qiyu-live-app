package org.idea.live.im.core.server.handler;

import org.idea.live.im.core.server.common.ImMsg;

import io.netty.channel.ChannelHandlerContext;

public interface ImHandlerFactory {

    /**
     * @brief 按照imMsg的类型(code)去筛选不同的处理器
     *
     * @param ctx   [in] 上下文
     * @param imMsg [in] 输入的message
     */
    void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg);
}

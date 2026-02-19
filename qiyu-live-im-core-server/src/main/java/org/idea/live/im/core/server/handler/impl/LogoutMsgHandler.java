package org.idea.live.im.core.server.handler.impl;

import org.idea.live.im.core.server.common.ImMsg;
import org.idea.live.im.core.server.handler.SimplyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

/**
 * @brief 用户登出处理器
 */
public class LogoutMsgHandler implements SimplyHandler {

    private final static Logger logger = LoggerFactory.getLogger(LogoutMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        logger.info("user logout: " + msg);
        ctx.writeAndFlush(msg);
    }

}

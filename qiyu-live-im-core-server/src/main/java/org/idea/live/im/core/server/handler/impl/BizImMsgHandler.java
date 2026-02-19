package org.idea.live.im.core.server.handler.impl;

import org.idea.live.im.core.server.common.ImMsg;
import org.idea.live.im.core.server.handler.SimplyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

/**
 * @brief 业务消息处理器
 */
public class BizImMsgHandler implements SimplyHandler {

    private final static Logger logger = LoggerFactory.getLogger(BizImMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        logger.info("normal im message: " + msg);
        ctx.writeAndFlush(msg);
    }

}

package org.idea.live.im.core.server.handler.impl;

import org.idea.live.im.core.server.common.ImMsg;
import org.idea.live.im.core.server.handler.SimplyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

/**
 * @brief 心跳消息处理器
 */
public class HeartBeatMsgHandler implements SimplyHandler {

    private final static Logger logger = LoggerFactory.getLogger(HeartBeatMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        logger.info("heartbeat: " + msg);
        ctx.writeAndFlush(msg);
    }

}

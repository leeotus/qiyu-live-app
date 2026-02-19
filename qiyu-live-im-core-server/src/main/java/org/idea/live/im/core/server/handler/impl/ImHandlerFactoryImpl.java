package org.idea.live.im.core.server.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.idea.live.im.core.server.common.ImMsg;
import org.idea.live.im.core.server.handler.ImHandlerFactory;
import org.idea.live.im.core.server.handler.SimplyHandler;
import org.idea.live.im.interfaces.ImMsgCodeEnum;

import io.netty.channel.ChannelHandlerContext;

public class ImHandlerFactoryImpl implements ImHandlerFactory {

    private static Map<Integer, SimplyHandler> simpleHandlerMap = new HashMap<>();

    static {
        simpleHandlerMap.put(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), new LoginMsgHandler());
        simpleHandlerMap.put(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), new LogoutMsgHandler());
        simpleHandlerMap.put(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), new BizImMsgHandler());
        simpleHandlerMap.put(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), new HeartBeatMsgHandler());
    }

    @Override
    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg) {
        SimplyHandler handler = simpleHandlerMap.get(imMsg.getCode());
        if (handler == null) {
            throw new IllegalArgumentException("msg code error, current code is " + imMsg.getCode());
        }
        handler.handler(ctx, imMsg);
    }

}

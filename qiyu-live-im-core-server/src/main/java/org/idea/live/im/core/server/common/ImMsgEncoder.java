package org.idea.live.im.core.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @brief 处理消息的编码过程
 */
public class ImMsgEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ImMsg imMsg = (ImMsg) msg;
        out.writeShort(imMsg.getMagic());
        out.writeInt(imMsg.getCode());
        out.writeInt(imMsg.getLen());
        out.writeBytes(imMsg.getBody());
    }

}

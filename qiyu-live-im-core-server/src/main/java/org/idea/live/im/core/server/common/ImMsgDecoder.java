package org.idea.live.im.core.server.common;

import java.util.List;

import org.idea.live.im.interfaces.ImConstants;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @brief 消息解码器
 */
public class ImMsgDecoder extends ByteToMessageDecoder {

    private final int BASE_LEN = 2 + 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // bytebuffer内容的基本校验, code, magic, length
        if (byteBuf.readableBytes() >= BASE_LEN) {
            if (byteBuf.readShort() != ImConstants.DEFAULT_MAGIC) {
                // magic error
                ctx.close();
                return;
            }
            int code = byteBuf.readInt();
            int len = byteBuf.readInt();

            if (byteBuf.readableBytes() < len) {
                // 长度不对
                ctx.close();
                return;
            }

            byte[] body = new byte[len];
            byteBuf.readBytes(body);
            // 将bytebuf转换为immsg对象
            ImMsg imMsg = new ImMsg();
            imMsg.setMagic(ImConstants.DEFAULT_MAGIC);
            imMsg.setCode(code);
            imMsg.setLen(len);
            imMsg.setBody(body);

            out.add(imMsg);
        }
    }

}

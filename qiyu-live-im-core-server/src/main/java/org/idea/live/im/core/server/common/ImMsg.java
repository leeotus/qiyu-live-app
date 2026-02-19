package org.idea.live.im.core.server.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import org.idea.live.im.interfaces.ImConstants;

@Data
public class ImMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = -6567417873780541989L;

    // 魔数, 用于做基本校验
    private short magic;

    // 消息体的种类, 后续交给不同的handler去处理
    private int code;

    // 记录body的长度
    private int len;

    // 存储消息体的body
    private byte[] body;

    public static ImMsg build(int code, String body) {
        ImMsg imMsg = new ImMsg();
        imMsg.setMagic(ImConstants.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setLen(body.getBytes().length);
        imMsg.setBody(body.getBytes());
        return imMsg;
    }
}

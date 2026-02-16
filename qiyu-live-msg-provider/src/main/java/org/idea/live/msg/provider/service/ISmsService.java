package org.idea.live.msg.provider.service;

import org.idea.live.msg.dto.MsgCheckDTO;
import org.idea.live.msg.enums.MsgSendResultEnum;

public interface ISmsService {

    /**
     * 发送短信接口
     *
     * @param phone [in] 用户手机号字符串
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * 校验验证码
     *
     * @param phone [in] 用户手机号字符串
     * @param code [in] 验证码
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);

    /**
     * 插入一条短信验证码记录
     *
     * @param phone [in] 用户手机号字符串
     * @param code [in] 验证码
     */
    void insertOne(String phone, Integer code);
}

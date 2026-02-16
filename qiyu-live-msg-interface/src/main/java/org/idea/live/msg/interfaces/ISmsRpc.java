package org.idea.live.msg.interfaces;

import org.idea.live.msg.dto.MsgCheckDTO;
import org.idea.live.msg.enums.MsgSendResultEnum;

public interface ISmsRpc {

    /**
     * 发送短信登录验证码接口
     *
     * @param phone [in] 用户手机号字符串
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * 验证登录验证码
     *
     * @param phone [in] 用户手机号
     * @param code [in] 验证码
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);
}

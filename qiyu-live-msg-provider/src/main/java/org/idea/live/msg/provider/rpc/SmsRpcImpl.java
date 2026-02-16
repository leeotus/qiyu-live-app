package org.idea.live.msg.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.idea.live.msg.dto.MsgCheckDTO;
import org.idea.live.msg.enums.MsgSendResultEnum;
import org.idea.live.msg.interfaces.ISmsRpc;
import org.idea.live.msg.provider.service.ISmsService;

@DubboService
public class SmsRpcImpl implements ISmsRpc {

    @Resource
    private ISmsService smsService;


    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return smsService.checkLoginCode(phone, code);
    }
}

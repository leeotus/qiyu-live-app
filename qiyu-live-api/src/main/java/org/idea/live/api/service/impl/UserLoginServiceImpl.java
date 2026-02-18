package org.idea.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.idea.live.api.service.IUserLoginService;
import org.idea.live.common.interfaces.vo.WebResponseVO;
import org.idea.live.msg.interfaces.ISmsRpc;
import org.idea.live.user.interfaces.IUserPhoneRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;

public class UserLoginServiceImpl implements IUserLoginService {

    private static String PHONE_REG = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);
    
    @DubboReference
    private ISmsRpc smsRpc;
    @DubboReference
    private IUserPhoneRPC userPhoneRPC;

    @Override
    public WebResponseVO sendLoginCode(String phone) {
        return null;
    }

    @Override
    public WebResponseVO login(String phone, Integer code, HttpServletResponse response) {
        return null;
    }
    
}

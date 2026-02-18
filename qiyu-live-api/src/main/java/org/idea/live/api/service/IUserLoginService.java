package org.idea.live.api.service;

import org.idea.live.common.interfaces.vo.WebResponseVO;

import jakarta.servlet.http.HttpServletResponse;

public interface IUserLoginService {
    
    /**
     * 发送登录验证码
     * @param phone [in] 用户手机号码字符串
     * @return
     */
    WebResponseVO sendLoginCode(String phone);
    
    /**
     * 手机号+验证码登录
     * @param phone [in] 用户手机号码字符串
     * @param code [in] 登录验证码
     * @param response
     * @return
     */
    WebResponseVO login(String phone, Integer code, HttpServletResponse response);
    
}

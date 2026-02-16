package org.idea.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.idea.live.user.dto.UserLoginDTO;
import org.idea.live.user.dto.UserPhoneDTO;
import org.idea.live.user.interfaces.IUserPhoneRPC;
import org.idea.live.user.provider.service.IUserPhoneService;

import java.util.List;

@DubboService
public class UserPhoneRpcImpl implements IUserPhoneRPC {

    @Resource
    private IUserPhoneService userPhoneService;

    @Override
    public UserLoginDTO login(String phone) {
        return userPhoneService.login(phone);
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        return userPhoneService.queryByPhone(phone);
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        return userPhoneService.queryByUserId(userId);
    }
}

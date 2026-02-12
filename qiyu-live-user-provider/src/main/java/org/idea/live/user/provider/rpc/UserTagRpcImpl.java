package org.idea.live.user.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.idea.live.user.constants.UserTagsEnum;
import org.idea.live.user.interfaces.IUserTagRpc;
import org.idea.live.user.provider.service.IUserTagService;

import jakarta.annotation.Resource;

@DubboService
public class UserTagRpcImpl implements IUserTagRpc {
    
    @Resource
    private IUserTagService userTagService;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.setTag(userId, userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.cancelTag(userId, userTagsEnum);
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.containTag(userId, userTagsEnum);
    }
    
}

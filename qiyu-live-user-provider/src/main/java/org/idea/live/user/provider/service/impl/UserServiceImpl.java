package org.idea.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.idea.live.common.interfaces.utils.ConvertBeanUtils;
import org.idea.live.dto.UserDTO;
import org.idea.live.user.provider.dao.mapper.IUserMapper;
import org.idea.live.user.provider.dao.po.UserPO;
import org.idea.live.user.provider.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Override
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return UserDTO 用户信息传输对象，如果用户ID为null则返回null
     */
    public UserDTO getByUserId(Long userId) {
        if(userId == null) { // 检查用户ID是否为null
            return null; // 如果为null则直接返回null
        }
        // 使用ConvertBeanUtils将User实体转换为UserDTO对象
        UserDTO userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        return userDTO; // 返回转换后的UserDTO对象
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        // TODO: 查重
        UserPO userPo = ConvertBeanUtils.convert(userDTO, UserPO.class);
        userMapper.insert(userPo);
        return true;
    }
}

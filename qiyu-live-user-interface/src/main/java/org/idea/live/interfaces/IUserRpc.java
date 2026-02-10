package org.idea.live.interfaces;

import org.idea.live.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface IUserRpc {

    /**
     * @brief RPC调用测试接口函数
     * @return 返回字符串
     */
    String test();

    /**
     * 根据用户ID获取用户数据传输对象
     * @param userId 用户ID，用于标识特定用户
     * @return UserDTO 包含用户信息的DTO对象，如果未找到则返回null
     */
    UserDTO getByUserId(Long userId);

    /**
     * 更新用户信息的方法
     * @param userDTO 包含用户更新信息的DTO对象
     * @return 更新操作是否成功，返回true表示成功，false表示失败
     */
    Boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入单个用户信息的方法
     * @param userDTO 包含用户信息的DTO对象，用于封装和传输用户数据
     * @return UserDTO 返回插入后的用户信息，成功返回true，失败返回false
     */
    boolean insertOne(UserDTO userDTO);

    /**
     * 批量查询用户信息的方法
     *
     * @param userIdList 用户ID列表，用于批量查询用户信息
     * @return 返回一个Map，键为用户ID(Long类型)，值为对应的用户信息(UserDTO类型)
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList);
}

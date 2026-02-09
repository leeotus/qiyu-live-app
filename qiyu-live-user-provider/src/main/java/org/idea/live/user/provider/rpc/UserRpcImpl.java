package org.idea.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.idea.live.dto.UserDTO;
import org.idea.live.interfaces.IUserRpc;
import org.idea.live.user.provider.service.IUserService;

/**
 * Dubbo服务实现类，标记为Dubbo服务
 * 实现IUserRpc接口，提供用户相关的远程调用服务
 */
@DubboService  // Dubbo服务注解，将该类暴露为Dubbo服务
public class UserRpcImpl implements IUserRpc {  // 实现IUserRpc接口，定义用户远程调用服务的具体实现

    @Resource  // 使用@Resource注解注入IUserService实例，用于处理用户相关的业务逻辑
    private IUserService userService;

    @Override  // 重写test方法，提供简单的测试接口
    public String test() {
        System.out.println("hello dubbo");  // 输出简单的日志信息
        return "success";  // 返回成功字符串
    }

    @Override  // 重写getByUserId方法，根据用户ID获取用户信息
    public UserDTO getByUserId(Long userId) {  // 接收用户ID作为参数，返回UserDTO对象
        return userService.getByUserId(userId);  // 调用userService的方法获取用户信息并返回
    }

    @Override
    public Boolean updateUserInfo(UserDTO userDTO) {
    // 调用userService的updateUserInfo方法执行实际的更新操作
        return userService.updateUserInfo(userDTO);
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        return userService.insertOne(userDTO);
    }


}

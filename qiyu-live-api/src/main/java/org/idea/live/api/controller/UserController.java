package org.idea.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.idea.live.dto.UserDTO;
import org.idea.live.interfaces.IUserRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试控制器
 * 使用@RestController注解标记这是一个RESTful控制器
 * @RequestMapping("/test")指定了该控制器的基础路径
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 注入IUserRpc服务
     * 使用@DubboReference注解标记这是一个Dubbo服务引用
     * 通过该引用可以调用远程服务的方法
     */
    @DubboReference(timeout = 5000, retries = 0)
    private IUserRpc userRpc;

    /**
     * 处理GET请求的方法
     * @GetMapping("/dubbo")指定了该方法的访问路径为/dubbo
     * @return 返回userRpc.test()方法的执行结果
     */
    @GetMapping("/dubbo")
    public String dubboTest() {
        return userRpc.test();
    }

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId) {
        return userRpc.getByUserId(userId);
    }

    /**
     * 批量查询用户信息的接口方法
     * 根据传入的用户ID字符串，返回一个以用户ID为键，UserDTO为值的Map
     *
     * @param userIdStr 逗号分隔的用户ID字符串，例如 "1,2,3"
     * @return 包含用户信息的Map，键为用户ID(Long类型)，值为对应的UserDTO对象
     * @example http://localhost:8080/user/batchQueryUserInfo?userIdStr=1,2,3
     */
    @GetMapping("/batchQueryUserInfo")
    public Map<Long, UserDTO> batchQueryUserInfo(String userIdStr) {
        // 将逗号分隔的字符串转换为List<Long>，然后调用RPC服务批量查询用户信息
        return userRpc.batchQueryUserInfo(Arrays.asList(userIdStr.split(",")).
                stream().map(x -> Long.valueOf(x)).collect(Collectors.toList()));
    }

    @GetMapping("/updateUserInfo")
    public boolean getUserInfo(Long userId, String nickname) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickname);
        return userRpc.updateUserInfo(userDTO);
    }

    @GetMapping("/insertOne")
    public boolean insertOne(Long userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName("idea-test");
        userDTO.setSex(1);
        return userRpc.insertOne(userDTO);
    }
}

package org.idea.live.user.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.idea.live.common.interfaces.enums.CommonStatusEnum;
import org.idea.live.common.interfaces.utils.ConvertBeanUtils;
import org.idea.live.common.interfaces.utils.DESUtils;
import org.idea.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.idea.live.id.generate.enums.IdTypeEnum;
import org.idea.live.id.generate.interfaces.IdGenerateRpc;
import org.idea.live.user.dto.UserDTO;
import org.idea.live.user.dto.UserLoginDTO;
import org.idea.live.user.dto.UserPhoneDTO;
import org.idea.live.user.provider.dao.mapper.IUserPhoneMapper;
import org.idea.live.user.provider.dao.po.UserPhonePO;
import org.idea.live.user.provider.service.IUserPhoneService;
import org.idea.live.user.provider.service.IUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserPhoneServiceImpl implements IUserPhoneService {

    @Resource
    private IUserPhoneMapper userPhoneMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    @Resource
    private IUserService userService;

    @DubboReference
    private IdGenerateRpc idGenerateRpc;

    /**
     * 用户登录（底层会进行手机号的注册）
     *
     * @param phone [in] 用户手机号
     * @return
     */
    @Override
    public UserLoginDTO login(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        // 是否注册过
        UserPhoneDTO userPhoneDTO = this.queryByPhone(phone);
        // 如果注册过, 创建token, 返回userId
        if (userPhoneDTO != null) {
            return UserLoginDTO.loginSuccess(userPhoneDTO.getUserId());
        }
        return registerAndLogin(phone);
    }

    /**
     * 注册并登录方法
     * 根据手机号进行用户注册，并完成登录流程
     *
     * @param phone 用户手机号，用于注册和登录
     * @return UserLoginDTO 登录后的数据传输对象，包含用户登录信息
     */
    private UserLoginDTO registerAndLogin(String phone) {
        Long userId = idGenerateRpc.getUnSeqId(IdTypeEnum.USER_USQ_ID.getCode());
        UserDTO userDTO = new UserDTO();
        userDTO.setNickName("qiyu-" + userId);
        userDTO.setUserId(userId);
        userService.insertOne(userDTO);
        UserPhonePO userPhonePO = new UserPhonePO();
        userPhonePO.setUserId(userId);
        userPhonePO.setPhone(DESUtils.encrypt(phone));
        userPhonePO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        userPhoneMapper.insert(userPhonePO);
        redisTemplate.delete(cacheKeyBuilder.buildUserPhoneObjKey(phone));
        return UserLoginDTO.loginSuccess(userId);
    }

    /**
     * 根据手机信息查询相关用户信息
     *
     * @param phone
     * @return
     */
    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneObjKey(phone);
        UserPhoneDTO userPhoneDTO = (UserPhoneDTO) redisTemplate.opsForValue().get(redisKey);
        if (userPhoneDTO != null) {
            //属于空值缓存对象
            if (userPhoneDTO.getUserId() == null) {
                return null;
            }
            return userPhoneDTO;
        }
        userPhoneDTO = this.queryByPhoneFromDB(phone);
        if (userPhoneDTO != null) {
            userPhoneDTO.setPhone(DESUtils.decrypt(userPhoneDTO.getPhone()));
            redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 30, TimeUnit.MINUTES);
            return userPhoneDTO;
        }
        //缓存击穿，空值缓存
        userPhoneDTO = new UserPhoneDTO();
        redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 5, TimeUnit.MINUTES);
        return null;
    }

    /**
     * 根据手机号从数据库查询用户信息
     * @param phone 手机号
     * @return UserPhoneDTO 用户手机信息传输对象
     */
    private UserPhoneDTO queryByPhoneFromDB(String phone) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getPhone, DESUtils.encrypt(phone));
        // 设置查询条件：状态码为有效状态
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        // 限制查询结果只返回一条记录
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(userPhoneMapper.selectOne(queryWrapper), UserPhoneDTO.class);
    }

    /**
     * 根据用户id查询手机相关信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        if (userId == null || userId < 10000) {
            return Collections.emptyList();
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneListKey(userId);
        List<Object> userPhoneList = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (!CollectionUtils.isEmpty(userPhoneList)) {
            //证明是空值缓存
            if (((UserPhoneDTO) userPhoneList.get(0)).getUserId() == null) {
                return Collections.emptyList();
            }
            return userPhoneList.stream().map(x -> (UserPhoneDTO) x).collect(Collectors.toList());
        }
        List<UserPhoneDTO> userPhoneDTOS = this.queryByUserIdFromDB(userId);
        if (!CollectionUtils.isEmpty(userPhoneDTOS)) {
            userPhoneDTOS.stream().forEach(x -> x.setPhone(DESUtils.decrypt(x.getPhone())));
            redisTemplate.opsForList().leftPushAll(redisKey, userPhoneDTOS.toArray());
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
            return userPhoneDTOS;
        }
        //缓存击穿，空对象缓存
        redisTemplate.opsForList().leftPush(redisKey, new UserPhoneDTO());
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        return Collections.emptyList();
    }

    /**
     * 根据用户id查询记录
     * @param userId
     * @return
     */
    private List<UserPhoneDTO> queryByUserIdFromDB(Long userId) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getUserId, userId);
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convertList(userPhoneMapper.selectList(queryWrapper), UserPhoneDTO.class);
    }
}

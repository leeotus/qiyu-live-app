package org.idea.live.user.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.idea.live.common.interfaces.topic.UserProviderTopicNames;
import org.idea.live.common.interfaces.utils.ConvertBeanUtils;
import org.idea.live.user.constants.CacheAsyncDeleteCode;
import org.idea.live.user.dto.UserCacheAsyncDeleteDTO;
import org.idea.live.user.dto.UserDTO;
import org.idea.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.idea.live.user.provider.dao.mapper.IUserMapper;
import org.idea.live.user.provider.dao.po.UserPO;
import org.idea.live.user.provider.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    @Autowired
    private Mapper mapper;

    @Resource
    private MQProducer mqProducer;

    @Override
    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return UserDTO 用户信息传输对象，如果用户ID为null则返回null
     */
    public UserDTO getByUserId(Long userId) {
        if (userId == null) { // 检查用户ID是否为null
            return null; // 如果为null则直接返回null
        }
        String key = cacheKeyBuilder.buildUserInfoKey(userId);
        UserDTO userDTO = redisTemplate.opsForValue().get(key); // 尝试从Redis中获取用户信息
        if (userDTO != null) {
            return userDTO; // 如果Redis中存在用户信息，则直接返回
        }
        // 否则使用ConvertBeanUtils将User实体转换为UserDTO对象
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        // 设置到redis:
        if (userDTO != null) {
            redisTemplate.opsForValue().set(key, userDTO, 30, TimeUnit.MINUTES);
        }
        return userDTO; // 返回转换后的UserDTO对象
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        String key = cacheKeyBuilder.buildUserInfoKey(userDTO.getUserId());
        redisTemplate.delete(key);
        // 延迟双删
        UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
        userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteCode.USER_INFO_DELETE.getCode());
        Map<String, Object> jsonParam = new HashMap<>();
        jsonParam.put("userId", userDTO.getUserId());
        userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(jsonParam));
        Message message = new Message();
        message.setTopic(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC);
        message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
        message.setDelayTimeLevel(1);   // 延迟级别, 1s
        try {
            mqProducer.send(message);   // 生成者发送消息
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    /**
     * 批量查询用户信息
     * 
     * @param userIdList 用户ID列表
     * @return 返回用户ID到UserDTO的映射Map
     */
    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        // 检查输入参数是否为空，如果为空则返回空Map
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }
        // NOTE: 默认每个用户的id至少都是大于10000的
        // 过滤掉ID小于等于10000的用户ID
        userIdList = userIdList.stream().filter(id -> id > 1000).collect(Collectors.toList());
        // 再次检查过滤后的列表是否为空，如果为空则返回空Map
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }

        // ### 从redis中查询
        List<String> keyList = new ArrayList<>();
        userIdList.forEach(userId -> {
            keyList.add(cacheKeyBuilder.buildUserInfoKey(userId));
        });
        List<UserDTO> userDTOList = redisTemplate.opsForValue().multiGet(keyList).stream()
                .filter(x -> x != null).collect(Collectors.toList());
        if (userDTOList.size() == userIdList.size()) {
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, user -> user));
        }

        // 没有在redis中找到的还得去mysql里找
        List<Long> userIdInCacheList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());
        List<Long> userIdNotInCacheList = userIdList.stream().filter(id -> !userIdInCacheList.contains(id))
                .collect(Collectors.toList());

        // ### 从mysql中查询

        // NOTE: 在本地开启多个线程，将不同表的id分成不同的List，然后分别查询，最后在本地内存做归并，避免直接使用如下代码(替换union all)
        // userMapper.selectBatchIds(userIdList);
        // 根据userId对100取模的结果进行分组，将ID分配到不同的分组中
        Map<Long, List<Long>> collect = userIdNotInCacheList.stream()
                .collect(Collectors.groupingBy(userId -> userId % 100));
        // 使用CopyOnWriteArrayList存放查询结果，适合并发场景
        List<UserDTO> queryUserDTOList = new CopyOnWriteArrayList<>(); // 存放结果
        // 使用并行流处理各个分组的查询，提高查询效率
        collect.values().parallelStream().forEach(queryUserIdList -> {
            // 查询每个分组中的用户信息，并转换为UserDTO对象
            List<UserPO> res = userMapper.selectBatchIds(queryUserIdList);
            queryUserDTOList.addAll(ConvertBeanUtils.convertList(res, UserDTO.class));
        });

        if (!CollectionUtils.isEmpty(queryUserDTOList)) {
            // RESEARCH: TODO: 从mysql中查找到的数据要写入到Redis??? -> 要看业务场景
            Map<String, UserDTO> saveCacheMap = queryUserDTOList.stream().collect(
                    Collectors.toMap(userDTO -> cacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()), user -> user));
            redisTemplate.opsForValue().multiSet(saveCacheMap);

            // 使用Redis管道批量操作, 提高性能
            redisTemplate.executePipelined(new SessionCallback<Object>() {

                @Override
                public <K, V> Object execute(RedisOperations<K, V> operation) throws DataAccessException {
                    for (String redisKey : saveCacheMap.keySet()) {
                        operation.expire((K) redisKey, createRandomExpireTime(), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });
            userDTOList.addAll(queryUserDTOList);
        }

        // 将查询结果转换为用户ID到UserDTO的Map返回
        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, user -> user));
    }
    
    /**
     * 创建一个随机过期时间
     * 
     * @return 返回计算后的过期时间（秒）
     *         随机生成一个1000以内的随机数，并加上30分钟（60*30秒）作为基础过期时间
     */
    private int createRandomExpireTime() {
        // 使用ThreadLocalRandom生成一个1000以内的随机数
        int time = ThreadLocalRandom.current().nextInt(1000);
        // 将随机数与30分钟（60*30秒）相加，作为最终的过期时间
        return time + 60 * 30;
    }
}

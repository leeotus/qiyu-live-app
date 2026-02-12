package org.idea.live.user.provider.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.idea.live.common.interfaces.topic.UserProviderTopicNames;
import org.idea.live.common.interfaces.utils.ConvertBeanUtils;
import org.idea.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.idea.live.user.constants.CacheAsyncDeleteCode;
import org.idea.live.user.constants.UserTagFieldNameConstants;
import org.idea.live.user.constants.UserTagsEnum;
import org.idea.live.user.dto.UserCacheAsyncDeleteDTO;
import org.idea.live.user.dto.UserDTO;
import org.idea.live.user.dto.UserTagDTO;
import org.idea.live.user.provider.dao.mapper.IUserTagMapper;
import org.idea.live.user.provider.dao.po.UserTagPO;
import org.idea.live.user.provider.service.IUserTagService;
import org.idea.live.user.utils.TagInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import jakarta.annotation.Resource;
import lombok.val;

/**
 * 用户标签服务实现类
 * 实现了IUserTagService接口，提供用户标签的设置、取消和查询功能
 */
@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private IUserTagMapper userTagMapper;

    @Resource
    private RedisTemplate<String, UserTagDTO> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;
    
    @Resource
    private MQProducer mqProducer;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        // 查询该用户有无标签
        boolean ret = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (ret) {
            deleteUserTagDTOFromRedis(userId);
            return true;
        }
        
        String cacheKey = cacheKeyBuilder.buildTagLockKey(userId);
        // 用户无标签，尝试加分布式锁
        String setNxResult = redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                String ret = (String) connection.execute("set",
                        keySerializer.serialize(cacheKey),
                        valueSerializer.serialize("-1"),
                        "NX".getBytes(StandardCharsets.UTF_8),
                        "EX".getBytes(StandardCharsets.UTF_8),
                        "3".getBytes(StandardCharsets.UTF_8));
                return ret;
            }
            
        });
        
        if(!"OK".equals(setNxResult)) {
            return false;
        }

        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if (userTagPO != null) {
            return false;
        }
        UserTagPO newPo = new UserTagPO();
        newPo.setUserId(userId);
        userTagMapper.insert(newPo);
        ret = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        redisTemplate.delete(cacheKey);
        return ret;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean ret = userTagMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (!ret) {
            // 删除失败
            return false;
        }
        
        // 清理缓存
        deleteUserTagDTOFromRedis(userId);

        return true;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagDTO userTagDTO = this.queryByUserIdFromRedis(userId);
        if (userTagDTO == null) {
            return false;
        }
        String fieldName = userTagsEnum.getFieldName();
        if (UserTagFieldNameConstants.TAG_INFO_01.equals(fieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo01(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstants.TAG_INFO_02.equals(fieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo02(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstants.TAG_INFO_03.equals(fieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo03(), userTagsEnum.getTag());
        }
        return false;
    }
    
    /**
     * 从Redis中删除用户标签信息
     * 
     * @param userId [in] 用户ID
     */
    private void deleteUserTagDTOFromRedis(Long userId) {
        String cacheKey = cacheKeyBuilder.buildTagKey(userId);
        redisTemplate.delete(cacheKey);
        // 延迟双删
        UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
        userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteCode.USER_TAG_DELETE.getCode());
        Map<String, Object> jsonParam = new HashMap<>();
        jsonParam.put("userId", userId);
        userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(jsonParam));

        Message message = new Message();
        message.setTopic(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC);
        message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
        // 延迟1s
        message.setDelayTimeLevel(1);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    
    /**
     * 从Redis中查询用户标签信息
     * 
     * @param userId [in] 用户ID
     */
    private UserTagDTO queryByUserIdFromRedis(Long userId) {
        String cacheKey = cacheKeyBuilder.buildTagKey(userId);
        UserTagDTO userTagDTO = redisTemplate.opsForValue().get(cacheKey);
        if (userTagDTO != null) {
            // 如果redis不为空
            return userTagDTO;
        }
        
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if (userTagPO == null) {
            return null;
        }

        userTagDTO = ConvertBeanUtils.convert(userTagPO, UserTagDTO.class);
        // 如果在mysql里找到了,还需要放入得到Redis中
        redisTemplate.opsForValue().set(cacheKey, userTagDTO);

        return userTagDTO;
    }
}
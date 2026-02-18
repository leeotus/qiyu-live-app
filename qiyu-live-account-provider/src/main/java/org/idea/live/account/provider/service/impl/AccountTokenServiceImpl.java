package org.idea.live.account.provider.service.impl;

import jakarta.annotation.Resource;
import org.idea.live.account.provider.config.AccountProviderCacheKeyBuilder;
import org.idea.live.account.provider.service.IAccountTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AccountTokenServiceImpl implements IAccountTokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AccountProviderCacheKeyBuilder cacheKeyBuilder;

    /**
     * 创建一个登录token
     *
     * @param userId
     * @return
     */
    @Override
    public String createAndSaveLoginToken(Long userId) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(cacheKeyBuilder.
                buildUserLoginTokenKey(token), String.valueOf(userId), 30, TimeUnit.DAYS);
        return token;
    }

    /**
     * 校验用户token
     *
     * @param tokenKey
     * @return
     */
    @Override
    public Long getUserIdByToken(String tokenKey) {
        String redisKey = cacheKeyBuilder.buildUserLoginTokenKey(tokenKey);
        Integer userId = (Integer) redisTemplate.opsForValue().get(redisKey);
        return userId == null ? null : Long.valueOf(userId);
    }
}

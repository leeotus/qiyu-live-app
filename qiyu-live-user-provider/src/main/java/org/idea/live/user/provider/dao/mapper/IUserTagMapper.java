package org.idea.live.user.provider.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.idea.live.user.provider.dao.po.UserTagPO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface IUserTagMapper extends BaseMapper<UserTagPO> {

    @Update("update t_user_tag set ${fieldName}=${fieldName} | #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=0")
    int setTag(Long userId, String fieldName, long tag);
    
    @Update("update t_user_tag set ${fieldName}=${fieldName} &~ #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=#{tag}")
    int cancelTag(Long userId, String fieldName, long tag);
    
}

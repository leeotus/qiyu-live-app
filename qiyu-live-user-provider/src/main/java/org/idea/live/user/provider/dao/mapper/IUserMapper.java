package org.idea.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.idea.live.user.provider.dao.po.UserPO;

/**
 * 用户数据访问接口
 * 该接口继承自BaseMapper，提供了对UserPO实体的基本数据库操作方法
 * 通过@Mapper注解标记为MyBatis的数据映射接口
 */
@Mapper
public interface IUserMapper extends BaseMapper<UserPO> {

}

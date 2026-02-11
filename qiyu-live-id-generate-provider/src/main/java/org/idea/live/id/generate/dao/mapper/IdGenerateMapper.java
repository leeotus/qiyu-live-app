package org.idea.live.id.generate.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.idea.live.id.generate.dao.po.IdGeneratePO;

import java.util.List;

@Mapper
public interface IdGenerateMapper extends BaseMapper<IdGeneratePO> {

    /**
     * 更新ID生成配置的计数器和版本号
     * 该方法用于在ID生成器中更新配置信息，包括增加下一个阈值和当前起始值，并递增版本号
     *
     * @param id 需要更新的配置ID
     * @param version 当前配置的版本号，用于乐观锁控制
     * @return int 返回更新的行数，通常为1表示更新成功，0表示未更新（版本不匹配等情况）
     */
    @Update("update t_id_generate_config set next_threshold=next_threshold+step," +
            "current_start=current_start+step,version=version+1 where id =#{id} and version=#{version}")
    int updateNewIdCountAndVersion(@Param("id")int id, @Param("version")int version);

    @Select("select * from t_id_generate_config")
    List<IdGeneratePO> selectAll();

}

package org.idea.live.id.generate.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_id_generate_config")
public class IdGeneratePO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * id备注描述
     */
    private String remark;

    /**
     * 初始化值
     */
    private long initNum;

    /**
     * 步长
     */
    private int step;

    /**
     * 是否是有序的id
     */
    private int isSeq;

    /**
     * 当前id所在阶段的开始值
     */
    private long currentStart;

    /**
     * 当前id所在阶段的阈值
     */
    private long nextThreshold;

    /**
     * 业务代码前缀
     */
    private String idPrefix;

    /**
     * 乐观锁版本号
     */
    private int version;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return "IdGeneratePO{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", initNum=" + initNum +
                ", step=" + step +
                ", isSeq=" + isSeq +
                ", currentStart=" + currentStart +
                ", nextThreshold=" + nextThreshold +
                ", idPrefix='" + idPrefix + '\'' +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
package org.idea.live.user.provider.dao.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_user_tag")
public class UserTagPO {
    @TableId(type = IdType.INPUT)
    private Long userId;

    @TableField(value = "tag_info_01")
    private Long tagInfo01;

    @TableField(value = "tag_info_02")
    private Long tagInfo02; // 留用tag字段

    @TableField(value = "tag_info_03")
    private Long tagInfo03; // 留用tag字段

    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return "UserTagPO{" +
                "userId=" + userId +
                ", tagInfo01=" + tagInfo01 +
                ", tagInfo02=" + tagInfo02 +
                ", tagInfo03=" + tagInfo03 +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

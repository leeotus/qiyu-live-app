package org.idea.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_phone")
public class UserPhonePO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String phone;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return "UserPhonePO{" +
                "id=" + id +
                ", userId=" + userId +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

package org.idea.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户手机数据传输对象(Data Transfer Object)类
 * 该类实现了Serializable接口，表示其对象可以被序列化
 */
@Data
public class UserPhoneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4502843195713255060L; // 序列化版本UID，用于版本控制

    private Long id;
    private Long userId;
    private String phone;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return "UserPhoneDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

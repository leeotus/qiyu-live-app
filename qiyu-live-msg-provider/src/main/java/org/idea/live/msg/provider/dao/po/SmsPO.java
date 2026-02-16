package org.idea.live.msg.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_sms")
public class SmsPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer code;
    private String phone;
    private Date sendTime;
    private Date updateTime;

    @Override
    public String toString() {
        return "SmsPO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", phone='" + phone + '\'' +
                ", sendTime=" + sendTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

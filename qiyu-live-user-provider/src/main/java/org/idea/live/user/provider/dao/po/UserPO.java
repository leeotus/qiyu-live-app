package org.idea.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类，对应数据库表t_user
 * 使用@Data注解自动生成getter、setter等方法
 * 使用@TableName注解指定对应的数据库表名: t_user表
 */
@Data
@TableName("t_user")
public class UserPO {
    /**
     * 用户ID，主键
     * 使用@TableId注解指定为主键，类型为手动输入
     */
    @TableId(type = IdType.INPUT)
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String trueName;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 性别，1-男 2-女 0-未知
     */
    private Integer sex;
    /**
     * 工作城市ID
     */
    private Integer workCity;
    /**
     * 出生城市ID
     */
    private Integer bornCity;
    /**
     * 出生日期
     */
    private Date bornDate;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 重写toString方法，用于输出对象信息
     * @return 格式化后的用户信息字符串
     */
    @Override
    public String toString() {
        return "UserPO{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex=" + sex +
                ", workCity=" + workCity +
                ", bornCity=" + bornCity +
                ", bornDate=" + bornDate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

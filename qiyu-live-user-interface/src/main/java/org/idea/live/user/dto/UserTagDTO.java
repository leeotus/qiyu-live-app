package org.idea.live.user.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserTagDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6166428396297438134L;

    private Long userId;
    private Long tagInfo01;
    private Long tagInfo02; // 留用tag字段
    private Long tagInfo03; // 留用tag字段
    private Date createTime;
    private Date updateTime;  
}

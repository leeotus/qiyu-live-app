package org.idea.live.im.interfaces;

public enum ImMsgCodeEnum {

    IM_LOGIN_MSG(1001, "登录IM消息包"),
    IM_LOGOUT_MSG(1002, "登出IM消息包"),
    IM_BIZ_MSG(1003, "常规业务消息包"),
    IM_HEARTBEAT_MSG(1004, "心跳消息包");

    private int code;
    private String desc;

    ImMsgCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}

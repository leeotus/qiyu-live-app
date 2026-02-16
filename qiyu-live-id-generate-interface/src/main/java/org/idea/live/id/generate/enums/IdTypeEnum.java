package org.idea.live.id.generate.enums;

public enum IdTypeEnum {

    USER_SEQ_ID(1,"用户顺序id生成策略"),
    USER_USQ_ID(2, "用户无序id生成策略");

    int code;
    String desc;

    IdTypeEnum(int code, String desc) {
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

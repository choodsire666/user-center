package com.chood.usercenter.constant.enums;

/**
 * 用户相关枚举
 *
 * @author chood
 */
public enum UserEnums {
    ACCOUNT_NORMAL(0, "正常"),
    ACCOUNT_BAN(1, "冻结");

    private final Integer code;
    private final String description;

    UserEnums(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

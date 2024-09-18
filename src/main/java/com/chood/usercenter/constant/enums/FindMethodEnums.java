package com.chood.usercenter.constant.enums;

/**
 * 找回方式枚举类
 *
 * @author chood
 */
public enum FindMethodEnums {

    PHONE("phone"),
    EMAIL("email");

    private final String type;

    FindMethodEnums(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

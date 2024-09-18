package com.chood.usercenter.common;

/**
 * 自定义返回码枚举
 *
 * @author chood
 */
public enum ResultCode {
    SUCCESS(200, "成功!", "操作成功!"),
    ERROR(500, "服务器发生内部错误!", "服务器发生内部错误!"),
    PARAMS_ERROR(400, "参数错误!", "参数错误!"),
    NOT_LOGIN(404, "用户未登录!", "用户未登录"),
    NO_AUTH(403, "权限不足!", "权限不足!"),
    USER_INVALID(50001, "账号冻结！", "账号冻结！"),
    USER_NOT_EXIST(50002, "用户不存在！", "用户不存在！");

    private final int code;

    private final String msg;

    private final String description;

    ResultCode(Integer code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }
}

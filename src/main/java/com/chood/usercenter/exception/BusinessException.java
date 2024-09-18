package com.chood.usercenter.exception;

import com.chood.usercenter.common.ResultCode;

/**
 * 自定义业务异常类
 *
 * @author chood
 */
@SuppressWarnings(value = "unused")
public class BusinessException extends RuntimeException {

    /**
     * 业务状态码
     */
    private final int code;

    /**
     * 业务异常描述
     */
    private final String description;

    public BusinessException(String msg, int code, String description) {
        super(msg);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResultCode resultCode, String description) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.description = description;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.description = resultCode.getDescription();
    }

    public BusinessException(ResultCode resultCode, int code, String description) {
        super(resultCode.getMsg());
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResultCode resultCode, String msg, String description) {
        super(msg);
        this.code = resultCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

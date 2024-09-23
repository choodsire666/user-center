package com.chood.usercenter.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回类
 *
 * @author chood
 */
@Data
@ApiModel(value = "统一返回类", description = "统一返回类")
@SuppressWarnings(value = "unused")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private int code;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * 描述信息
     */
    @ApiModelProperty(value = "描述信息")
    private String description;

    /**
     * 防止使用构造函数
     */
    private Result() {
    }

    private Result(T data, int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> success(String description) {
        return new Result<>(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), description);
    }

    public static <T> Result<T> success(T data, String description) {
        return new Result<>(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), description);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    /**
     * 返回失败
     */
    public static <T> Result<T> error(Integer code, String msg, String description) {
        return new Result<>(null, code, msg, description);
    }

    public static <T> Result<T> error(Integer code) {
        return error(code, ResultCode.ERROR.getMsg(), null);
    }

    public static <T> Result<T> error(Integer code, String description) {
        return error(code, ResultCode.ERROR.getMsg(), description);
    }

    public static <T> Result<T> error(String msg) {
        return error(ResultCode.ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(String msg, String description) {
        return error(ResultCode.ERROR.getCode(), msg, description);
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMsg(), resultCode.getDescription());
    }

    public static <T> Result<T> error(ResultCode resultCode, String description) {
        return error(resultCode.getCode(), resultCode.getMsg(), description);
    }
}

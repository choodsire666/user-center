package com.chood.usercenter.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author chood
 */
@ApiModel(value = "用户注册请求体")
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码")
    private String checkPassword;
}

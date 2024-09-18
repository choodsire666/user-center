package com.chood.usercenter.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 编辑用户Query
 *
 * @author chood
 */
@Data
@ApiModel(value = "编辑用户Query", description = "编辑用户Query")
public class UserEditRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long id;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String username;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer gender;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    private Integer role;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
}

package com.chood.usercenter.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 重置密码请求实体
 *
 * @author chood
 */
@Data
@ApiModel(value = "重置密码请求实体", description = "重置密码请求实体")
public class ResetPasswordRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newPassword;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码")
    private String checkPassword;
}

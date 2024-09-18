package com.chood.usercenter.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 找回方式Request
 *
 * @author chood
 */
@Data
@ApiModel(value = "找回方式Request", description = "找回方式Request")
public class FindMethodRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 找回方式
     */
    @ApiModelProperty(value = "找回方式", notes = "找回方式")
    private String type;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", notes = "邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", notes = "手机号码")
    private String phone;
}

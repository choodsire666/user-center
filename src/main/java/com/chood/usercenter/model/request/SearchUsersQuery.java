package com.chood.usercenter.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 搜索用户Query
 *
 * @author chood
 */
@EqualsAndHashCode(callSuper = false)
@Data
@ApiModel(value = "搜索用户Query", description = "搜索用户Query")
public class SearchUsersQuery extends BasePageQuery implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号", notes = "用户账号")
    private String userAccount;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", notes = "用户名")
    private String username;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", notes = "性别")
    private Integer gender;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", notes = "状态")
    private Integer status;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", notes = "角色")
    private Integer role;

    /**
     * 创建时间排序方式
     */
    @ApiModelProperty(value = "创建时间排序方式", notes = "创建时间排序方式")
    private String createTime;
}

package com.chood.usercenter.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 基本分页Query
 *
 * @author chood
 */
@Data
@ApiModel(value = "基本分页Query", description = "基本分页Query")
public class BasePageQuery implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 第几页
     */
    @ApiModelProperty(value = "第几页", notes = "第几页")
    private Long current;

    /**
     * 一页的大小
     */
    @ApiModelProperty(value = "一页的大小", notes = "一页的大小")
    private Long pageSize;

    /**
     * 验证参数
     */
    public void valid() {
        if (current == null || current < 1) {
            current = 1L;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 5L;
        }
    }
}

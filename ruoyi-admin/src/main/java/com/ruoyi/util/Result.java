/**
 * 2018 Copyright (c)
 *
 * 
 *
 * 版权所有，侵权必究！
 */

package com.ruoyi.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author Admin_sk
 * @since 1.0.0
 */
@Data
@ApiModel(value = "响应")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    @ApiModelProperty(value = "编码：0表示成功，其他值表示失败")
    private int code = 0;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String msg = "success";
    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success(){
        return code == 0 ? true : false;
    }


    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

}

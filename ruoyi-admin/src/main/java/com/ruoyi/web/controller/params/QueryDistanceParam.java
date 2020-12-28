package com.ruoyi.web.controller.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className QueryDistanceParam.java
 * @createTime 2020年12月27日 21:45:00
 */
@ApiModel(value = "查询地址距离")
@Data
public class QueryDistanceParam {

    @ApiModelProperty(value = "地址A")
    private String address;
    @ApiModelProperty(value = "地址B")
    private String otherAddress;
}

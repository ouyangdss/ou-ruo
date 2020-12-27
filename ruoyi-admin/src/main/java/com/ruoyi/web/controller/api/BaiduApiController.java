package com.ruoyi.web.controller.api;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.alibaba.fastjson.JSON;
import com.ruoyi.framework.api.GeocodingService;
import com.ruoyi.util.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className BaiduApiController.java
 * @createTime 2020年12月26日 21:31:00
 */
@Slf4j
@Api(tags="百度api管理")
@RestController
@RequestMapping(value = "/wx", method = {RequestMethod.GET, RequestMethod.POST})
public class BaiduApiController {

    @Resource(name = "geocodingService")
    private GeocodingService geocodingService;

    @PostMapping("getLatAndLngByAddress")
    @ApiOperation("根据地址获取经纬度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = true, dataType = "String"),
    })
    public Result getLatAndLngByAddress(@RequestBody @Validated String address,BindingResult bindingResult){

        log.info("根据地址获取经纬度入参:{}" , address);

        Result result = new Result();

        if (bindingResult.hasErrors()) {
            result.error(ErrorCode.NOT_PARAMETERIZED,bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        try {
            Map<String, Double> latAndLngByAddress = geocodingService.getLatAndLngByAddress(address);
            result.setData(latAndLngByAddress);
        } catch (Exception e){
            log.error("根据地址获取经纬度异常" + e);
            result.error(ErrorCode.OTHER,"根据地址获取经纬度异常！");
        }
        return result;
    }
}

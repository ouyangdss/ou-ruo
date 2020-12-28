package com.ruoyi.web.controller.system;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.api.GeocodingService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.util.Result;
import com.ruoyi.web.controller.params.QueryDistanceParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@Slf4j
@Controller
@RequestMapping("/system/interface")
public class SysInterfaceController extends BaseController {
    private String prefix = "system/interface";

    @Autowired
    private ISysNoticeService noticeService;

    @RequiresPermissions("system:interface:view")
    @GetMapping()
    public String notice() {
        return prefix + "/interface";
    }

    @RequiresPermissions("system:interface:view")
    @GetMapping("/orderTrajectory")
    public String orderTrajectory() {
        return prefix + "/orderTrajectory";
    }

    @RequiresPermissions("system:interface:view")
    @GetMapping("/baiduApi")
    public String baiduApi() {
        return prefix + "/baiduApi";
    }

    /**
     * 查询公告列表
     */
    @RequiresPermissions("system:interface:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 新增公告
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存公告
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNotice notice) {
        notice.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改公告
     */
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存公告
     */
    @RequiresPermissions("system:interface:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotice notice) {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("system:interface:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(noticeService.deleteNoticeByIds(ids));
    }

    @Resource(name = "geocodingService")
    private GeocodingService geocodingService;

    @PostMapping("getLatAndLngByAddress")
    @ApiOperation("根据地址获取经纬度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = true, dataType = "String"),
    })
    @ResponseBody
    public Result getLatAndLngByAddress(@RequestBody @Validated String address, BindingResult bindingResult){

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

    @PostMapping("getDistanceFromTwoPlaces")
    @ApiOperation("计算两个地址的距离")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "地址A", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "otherAddress", value = "地址B", paramType = "query", required = true, dataType = "String"),
    })
    @ResponseBody
    public Result getDistanceFromTwoPlaces(@RequestBody @Validated QueryDistanceParam param, BindingResult bindingResult){
        log.info("计算两个地址的距离:{}" ,JSON.toJSONString(param));

        Result result = new Result();

        if (bindingResult.hasErrors()) {
            result.error(ErrorCode.NOT_PARAMETERIZED,bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        try {
            Double distanceFromTwoPlaces = geocodingService.getDistanceFromTwoPlaces(param.getAddress(), param.getOtherAddress());
            String des = "";
            if(distanceFromTwoPlaces !=null){
                BigDecimal distance = new BigDecimal(distanceFromTwoPlaces);
                if(distance.compareTo(new BigDecimal("100")) > 0){
                    distance = distance.divide(new BigDecimal("1000"));
                    des = "公里";
                } else {
                    des = "米";
                }
                distance = distance.setScale(2, BigDecimal.ROUND_HALF_UP);
                result.setData(distance+des);
            }
        } catch (Exception e){
            log.error("计算两个地址的距离异常" + e);
            result.error(ErrorCode.OTHER,"计算两个地址的距离异常！");
        }
        return result;
    }

}

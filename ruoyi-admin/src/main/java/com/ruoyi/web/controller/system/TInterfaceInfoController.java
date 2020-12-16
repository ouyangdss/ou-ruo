package com.ruoyi.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TInterfaceInfo;
import com.ruoyi.system.service.ITInterfaceInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【接口查询】Controller
 *
 * @author ruoyi
 * @date 2020-11-14
 */
@Controller
@RequestMapping("/system/info")
public class TInterfaceInfoController extends BaseController {
    private String prefix = "system/info";

    @Autowired
    private ITInterfaceInfoService tInterfaceInfoService;

    @RequiresPermissions("system:info:view")
    @GetMapping()
    public String info() {
        return prefix + "/info";
    }

    /**
     * 查询【接口名称】列表
     */
    @RequiresPermissions("system:info:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TInterfaceInfo tInterfaceInfo) {
        startPage();
        List<TInterfaceInfo> list = tInterfaceInfoService.selectTInterfaceInfoList(tInterfaceInfo);
        return getDataTable(list);
    }

    /**
     * 导出【接口名称】列表
     */
    @RequiresPermissions("system:info:export")
    @Log(title = "接口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TInterfaceInfo tInterfaceInfo) {
        List<TInterfaceInfo> list = tInterfaceInfoService.selectTInterfaceInfoList(tInterfaceInfo);
        ExcelUtil<TInterfaceInfo> util = new ExcelUtil<TInterfaceInfo>(TInterfaceInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 新增【接口名称】
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存【接口名称】
     */
    @RequiresPermissions("system:info:add")
    @Log(title = "接口", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TInterfaceInfo tInterfaceInfo) {
        return toAjax(tInterfaceInfoService.insertTInterfaceInfo(tInterfaceInfo));
    }

    /**
     * 修改【接口名称】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        TInterfaceInfo tInterfaceInfo = tInterfaceInfoService.selectTInterfaceInfoById(id);
        mmap.put("tInterfaceInfo", tInterfaceInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存【接口名称】
     */
    @RequiresPermissions("system:info:edit")
    @Log(title = "接口", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TInterfaceInfo tInterfaceInfo) {
        return toAjax(tInterfaceInfoService.updateTInterfaceInfo(tInterfaceInfo));
    }

    /**
     * 删除【接口名称】
     */
    @RequiresPermissions("system:info:remove")
    @Log(title = "接口", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(tInterfaceInfoService.deleteTInterfaceInfoByIds(ids));
    }
}

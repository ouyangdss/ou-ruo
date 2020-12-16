package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.ruoyi.common.utils.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import com.ruoyi.system.domain.DbDataDocConf;
import com.ruoyi.system.service.IDbDataDocConfService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * 数据库doc配置Controller
 *
 * @author ruoyi
 * @date 2020-12-11
 */
@Controller
@RequestMapping("/system/conf")
public class DbDataDocConfController extends BaseController {
    private String prefix = "system/conf";

    @Autowired
    private IDbDataDocConfService dbDataDocConfService;

    @RequiresPermissions("system:conf:view")
    @GetMapping()
    public String conf() {
        return prefix + "/conf";
    }

    /**
     * 查询数据库doc配置列表
     */
    @RequiresPermissions("system:conf:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DbDataDocConf dbDataDocConf) {
        startPage();
        List<DbDataDocConf> list = dbDataDocConfService.selectDbDataDocConfList(dbDataDocConf);
        return getDataTable(list);
    }

    /**
     * 导出数据库doc配置列表
     */
    @RequiresPermissions("system:conf:export")
    @Log(title = "数据库doc配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DbDataDocConf dbDataDocConf) {
        List<DbDataDocConf> list = dbDataDocConfService.selectDbDataDocConfList(dbDataDocConf);
        ExcelUtil<DbDataDocConf> util = new ExcelUtil<DbDataDocConf>(DbDataDocConf.class);
        return util.exportExcel(list, "conf");
    }

    /**
     * 新增数据库doc配置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存数据库doc配置
     */
    @RequiresPermissions("system:conf:add")
    @Log(title = "数据库doc配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DbDataDocConf dbDataDocConf) {
        return toAjax(dbDataDocConfService.insertDbDataDocConf(dbDataDocConf));
    }

    /**
     * 修改数据库doc配置
     */
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Integer configId, ModelMap mmap) {
        DbDataDocConf dbDataDocConf = dbDataDocConfService.selectDbDataDocConfById(configId);
        mmap.put("dbDataDocConf", dbDataDocConf);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据库doc配置
     */
    @RequiresPermissions("system:conf:edit")
    @Log(title = "数据库doc配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DbDataDocConf dbDataDocConf) {
        return toAjax(dbDataDocConfService.updateDbDataDocConf(dbDataDocConf));
    }

    /**
     * 删除数据库doc配置
     */
    @RequiresPermissions("system:conf:remove")
    @Log(title = "数据库doc配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(dbDataDocConfService.deleteDbDataDocConfByIds(ids));
    }

    /**
     * 生成DOC文档
     */
    @RequiresPermissions("system:conf:doc")
    @Log(title = "生成DOC文档", businessType = BusinessType.GENCODE)
    @GetMapping("/createDoc/{configId}")
    public void createDoc(HttpServletResponse response, @PathVariable(value = "configId", required = true) String configId) throws IOException {
        logger.info("生成DOC文档入参:{}", configId);
        //根据 configId 获取对应的配置信息
        DbDataDocConf dbDataDocConf = dbDataDocConfService.selectDbDataDocConfById(Integer.valueOf(configId));
        if (Objects.isNull(dbDataDocConf)) {
            logger.warn("数据库doc配置为空");
            return;
        }
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dbDataDocConf.getDriverClassName());
        hikariConfig.setJdbcUrl(dbDataDocConf.getJdbcUrl());
        hikariConfig.setUsername(dbDataDocConf.getUserName());
        hikariConfig.setPassword(dbDataDocConf.getPassWord());
        //设置可以获取tables remarks信息
        hikariConfig.setSchema(dbDataDocConf.getSchemaName());//设置想要导出的schema
        hikariConfig.addDataSourceProperty(dbDataDocConf.getSchemaName(), "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);

        EngineConfig engineConfig = EngineConfig.builder()
                //导出文件地址
                .fileOutputDir(dbDataDocConf.getFileOutputDir())
                //是否打开文件夹
                .openOutputDir(true)
                //文件类型:html、doc、mockdown
                .fileType(EngineFileType.WORD)
                //模板引擎
                .produceType(EngineTemplateType.freemarker).build();

//        ProcessConfig processConfig = ProcessConfig.builder()
//                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
//                //根据名称指定表生成
//                .designatedTableName(new ArrayList<>())
//                //根据表前缀生成
//                .designatedTablePrefix(new ArrayList<>())
//                //根据表后缀生成
//                .designatedTableSuffix(new ArrayList<>()).build();

        //设置生成pojo相关配置
        Configuration config = Configuration.builder()
                .version("1.0.0")
                .description("数据库设计文档")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig()).build();
        new DocumentationExecute(config).execute();
    }

    /**
     * 配置想要生成的表+ 配置想要忽略的表
     *
     * @return 生成表配置
     */
    public static ProcessConfig getProcessConfig() {
        // 忽略表名
        List<String> ignoreTableName = Arrays.asList();
        // 忽略表前缀，如忽略a开头的数据库表
        List<String> ignorePrefix = Arrays.asList();
        // 忽略表后缀
        List<String> ignoreSuffix = Arrays.asList();
        return ProcessConfig.builder()
                //根据名称指定表生成
//                .designatedTableName(Arrays.asList("user"))
                //根据表前缀生成("a")
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成("_user")
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
    }
}

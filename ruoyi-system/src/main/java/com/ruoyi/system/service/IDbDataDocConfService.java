package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.DbDataDocConf;

/**
 * 数据库doc配置Service接口
 *
 * @author ruoyi
 * @date 2020-12-11
 */
public interface IDbDataDocConfService {
    /**
     * 查询数据库doc配置
     *
     * @param configId 数据库doc配置ID
     * @return 数据库doc配置
     */
    public DbDataDocConf selectDbDataDocConfById(Integer configId);

    /**
     * 查询数据库doc配置列表
     *
     * @param dbDataDocConf 数据库doc配置
     * @return 数据库doc配置集合
     */
    public List<DbDataDocConf> selectDbDataDocConfList(DbDataDocConf dbDataDocConf);

    /**
     * 新增数据库doc配置
     *
     * @param dbDataDocConf 数据库doc配置
     * @return 结果
     */
    public int insertDbDataDocConf(DbDataDocConf dbDataDocConf);

    /**
     * 修改数据库doc配置
     *
     * @param dbDataDocConf 数据库doc配置
     * @return 结果
     */
    public int updateDbDataDocConf(DbDataDocConf dbDataDocConf);

    /**
     * 批量删除数据库doc配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDbDataDocConfByIds(String ids);

    /**
     * 删除数据库doc配置信息
     *
     * @param configId 数据库doc配置ID
     * @return 结果
     */
    public int deleteDbDataDocConfById(Integer configId);
}

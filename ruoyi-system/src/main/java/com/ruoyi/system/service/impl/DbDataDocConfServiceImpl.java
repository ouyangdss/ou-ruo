package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DbDataDocConfMapper;
import com.ruoyi.system.domain.DbDataDocConf;
import com.ruoyi.system.service.IDbDataDocConfService;
import com.ruoyi.common.core.text.Convert;

/**
 * 数据库doc配置Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-11
 */
@Service
public class DbDataDocConfServiceImpl implements IDbDataDocConfService {
    @Autowired
    private DbDataDocConfMapper dbDataDocConfMapper;

    /**
     * 查询数据库doc配置
     *
     * @param configId 数据库doc配置ID
     * @return 数据库doc配置
     */
    @Override
    public DbDataDocConf selectDbDataDocConfById(Integer configId) {
        return dbDataDocConfMapper.selectDbDataDocConfById(configId);
    }

    /**
     * 查询数据库doc配置列表
     *
     * @param dbDataDocConf 数据库doc配置
     * @return 数据库doc配置
     */
    @Override
    public List<DbDataDocConf> selectDbDataDocConfList(DbDataDocConf dbDataDocConf) {
        return dbDataDocConfMapper.selectDbDataDocConfList(dbDataDocConf);
    }

    /**
     * 新增数据库doc配置
     *
     * @param dbDataDocConf 数据库doc配置
     * @return 结果
     */
    @Override
    public int insertDbDataDocConf(DbDataDocConf dbDataDocConf) {
        dbDataDocConf.setCreateTime(DateUtils.getNowDate());
        return dbDataDocConfMapper.insertDbDataDocConf(dbDataDocConf);
    }

    /**
     * 修改数据库doc配置
     *
     * @param dbDataDocConf 数据库doc配置
     * @return 结果
     */
    @Override
    public int updateDbDataDocConf(DbDataDocConf dbDataDocConf) {
        dbDataDocConf.setUpdateTime(DateUtils.getNowDate());
        return dbDataDocConfMapper.updateDbDataDocConf(dbDataDocConf);
    }

    /**
     * 删除数据库doc配置对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDbDataDocConfByIds(String ids) {
        return dbDataDocConfMapper.deleteDbDataDocConfByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除数据库doc配置信息
     *
     * @param configId 数据库doc配置ID
     * @return 结果
     */
    @Override
    public int deleteDbDataDocConfById(Integer configId) {
        return dbDataDocConfMapper.deleteDbDataDocConfById(configId);
    }
}

package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TInterfaceInfoMapper;
import com.ruoyi.system.domain.TInterfaceInfo;
import com.ruoyi.system.service.ITInterfaceInfoService;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2020-11-14
 */
@Service
public class TInterfaceInfoServiceImpl implements ITInterfaceInfoService {
    @Autowired
    private TInterfaceInfoMapper tInterfaceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TInterfaceInfo selectTInterfaceInfoById(Long id) {
        return tInterfaceInfoMapper.selectTInterfaceInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tInterfaceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TInterfaceInfo> selectTInterfaceInfoList(TInterfaceInfo tInterfaceInfo) {
        return tInterfaceInfoMapper.selectTInterfaceInfoList(tInterfaceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param tInterfaceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTInterfaceInfo(TInterfaceInfo tInterfaceInfo) {
        tInterfaceInfo.setCreateTime(DateUtils.getNowDate());
        return tInterfaceInfoMapper.insertTInterfaceInfo(tInterfaceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param tInterfaceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTInterfaceInfo(TInterfaceInfo tInterfaceInfo) {
        return tInterfaceInfoMapper.updateTInterfaceInfo(tInterfaceInfo);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTInterfaceInfoByIds(String ids) {
        return tInterfaceInfoMapper.deleteTInterfaceInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTInterfaceInfoById(Long id) {
        return tInterfaceInfoMapper.deleteTInterfaceInfoById(id);
    }
}

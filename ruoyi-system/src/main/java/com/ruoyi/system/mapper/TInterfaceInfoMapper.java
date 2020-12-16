package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.TInterfaceInfo;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2020-11-14
 */
public interface TInterfaceInfoMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TInterfaceInfo selectTInterfaceInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tInterfaceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TInterfaceInfo> selectTInterfaceInfoList(TInterfaceInfo tInterfaceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param tInterfaceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertTInterfaceInfo(TInterfaceInfo tInterfaceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param tInterfaceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateTInterfaceInfo(TInterfaceInfo tInterfaceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTInterfaceInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTInterfaceInfoByIds(String[] ids);
}

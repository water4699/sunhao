package com.ruoyi.area.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.area.mapper.AreaJinanMapper;
import com.ruoyi.area.domain.AreaJinan;
import com.ruoyi.area.service.IAreaJinanService;

/**
 * 济南地区Service业务层处理
 */
@Service
public class AreaJinanServiceImpl implements IAreaJinanService 
{
    @Autowired
    private AreaJinanMapper areaJinanMapper;

    /**
     * 查询济南地区
     * 
     * @param areaId 济南地区主键
     * @return 济南地区
     */
    @Override
    public AreaJinan selectAreaJinanByAreaId(String areaId)
    {
        return areaJinanMapper.selectAreaJinanByAreaId(areaId);
    }

    /**
     * 查询济南地区列表
     * 
     * @param areaJinan 济南地区
     * @return 济南地区
     */
    @Override
    public List<AreaJinan> selectAreaJinanList(AreaJinan areaJinan)
    {
        return areaJinanMapper.selectAreaJinanList(areaJinan);
    }

    /**
     * 新增济南地区
     * 
     * @param areaJinan 济南地区
     * @return 结果
     */
    @Override
    public int insertAreaJinan(AreaJinan areaJinan)
    {
        return areaJinanMapper.insertAreaJinan(areaJinan);
    }

    /**
     * 修改济南地区
     * 
     * @param areaJinan 济南地区
     * @return 结果
     */
    @Override
    public int updateAreaJinan(AreaJinan areaJinan)
    {
        return areaJinanMapper.updateAreaJinan(areaJinan);
    }

    /**
     * 批量删除济南地区
     * 
     * @param areaIds 需要删除的济南地区主键
     * @return 结果
     */
    @Override
    public int deleteAreaJinanByAreaIds(String[] areaIds)
    {
        return areaJinanMapper.deleteAreaJinanByAreaIds(areaIds);
    }

    /**
     * 删除济南地区信息
     * 
     * @param areaId 济南地区主键
     * @return 结果
     */
    @Override
    public int deleteAreaJinanByAreaId(String areaId)
    {
        return areaJinanMapper.deleteAreaJinanByAreaId(areaId);
    }
}

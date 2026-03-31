package com.ruoyi.area.mapper;

import java.util.List;
import com.ruoyi.area.domain.AreaJinan;

/**
 * 济南地区Mapper接口

 */
public interface AreaJinanMapper 
{
    /**
     * 查询济南地区
     * 
     * @param areaId 济南地区主键
     * @return 济南地区
     */
    public AreaJinan selectAreaJinanByAreaId(String areaId);

    /**
     * 查询济南地区列表
     * 
     * @param areaJinan 济南地区
     * @return 济南地区集合
     */
    public List<AreaJinan> selectAreaJinanList(AreaJinan areaJinan);

    /**
     * 新增济南地区
     * 
     * @param areaJinan 济南地区
     * @return 结果
     */
    public int insertAreaJinan(AreaJinan areaJinan);

    /**
     * 修改济南地区
     * 
     * @param areaJinan 济南地区
     * @return 结果
     */
    public int updateAreaJinan(AreaJinan areaJinan);

    /**
     * 删除济南地区
     * 
     * @param areaId 济南地区主键
     * @return 结果
     */
    public int deleteAreaJinanByAreaId(String areaId);

    /**
     * 批量删除济南地区
     * 
     * @param areaIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAreaJinanByAreaIds(String[] areaIds);
}

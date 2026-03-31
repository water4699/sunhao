package com.ruoyi.grade.service;

import java.util.List;
import com.ruoyi.grade.domain.GradeLevel;

/**
 * 年级Service接口
 */
public interface IGradeLevelService 
{
    /**
     * 查询年级
     * 
     * @param gradeId 年级主键
     * @return 年级
     */
    public GradeLevel selectGradeLevelByGradeId(String gradeId);

    /**
     * 查询年级列表
     * 
     * @param gradeLevel 年级
     * @return 年级集合
     */
    public List<GradeLevel> selectGradeLevelList(GradeLevel gradeLevel);

    /**
     * 新增年级
     * 
     * @param gradeLevel 年级
     * @return 结果
     */
    public int insertGradeLevel(GradeLevel gradeLevel);

    /**
     * 修改年级
     * 
     * @param gradeLevel 年级
     * @return 结果
     */
    public int updateGradeLevel(GradeLevel gradeLevel);

    /**
     * 批量删除年级
     * 
     * @param gradeIds 需要删除的年级主键集合
     * @return 结果
     */
    public int deleteGradeLevelByGradeIds(String[] gradeIds);

    /**
     * 删除年级信息
     * 
     * @param gradeId 年级主键
     * @return 结果
     */
    public int deleteGradeLevelByGradeId(String gradeId);
}

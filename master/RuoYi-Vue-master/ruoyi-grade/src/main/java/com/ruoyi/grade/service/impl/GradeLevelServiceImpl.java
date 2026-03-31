package com.ruoyi.grade.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.grade.mapper.GradeLevelMapper;
import com.ruoyi.grade.domain.GradeLevel;
import com.ruoyi.grade.service.IGradeLevelService;

/**
 * 年级Service业务层处理
 */
@Service
public class GradeLevelServiceImpl implements IGradeLevelService 
{
    @Autowired
    private GradeLevelMapper gradeLevelMapper;

    /**
     * 查询年级
     * 
     * @param gradeId 年级主键
     * @return 年级
     */
    @Override
    public GradeLevel selectGradeLevelByGradeId(String gradeId)
    {
        return gradeLevelMapper.selectGradeLevelByGradeId(gradeId);
    }

    /**
     * 查询年级列表
     * 
     * @param gradeLevel 年级
     * @return 年级
     */
    @Override
    public List<GradeLevel> selectGradeLevelList(GradeLevel gradeLevel)
    {
        return gradeLevelMapper.selectGradeLevelList(gradeLevel);
    }

    /**
     * 新增年级
     * 
     * @param gradeLevel 年级
     * @return 结果
     */
    @Override
    public int insertGradeLevel(GradeLevel gradeLevel)
    {
        return gradeLevelMapper.insertGradeLevel(gradeLevel);
    }

    /**
     * 修改年级
     * 
     * @param gradeLevel 年级
     * @return 结果
     */
    @Override
    public int updateGradeLevel(GradeLevel gradeLevel)
    {
        return gradeLevelMapper.updateGradeLevel(gradeLevel);
    }

    /**
     * 批量删除年级
     * 
     * @param gradeIds 需要删除的年级主键
     * @return 结果
     */
    @Override
    public int deleteGradeLevelByGradeIds(String[] gradeIds)
    {
        return gradeLevelMapper.deleteGradeLevelByGradeIds(gradeIds);
    }

    /**
     * 删除年级信息
     * 
     * @param gradeId 年级主键
     * @return 结果
     */
    @Override
    public int deleteGradeLevelByGradeId(String gradeId)
    {
        return gradeLevelMapper.deleteGradeLevelByGradeId(gradeId);
    }
}

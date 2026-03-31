package com.ruoyi.subject.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.subject.mapper.SubjectMapper;
import com.ruoyi.subject.domain.Subject;
import com.ruoyi.subject.service.ISubjectService;

/**
 * 科目Service业务层处理
 */
@Service
public class SubjectServiceImpl implements ISubjectService 
{
    @Autowired
    private SubjectMapper subjectMapper;

    /**
     * 查询科目
     * 
     * @param subjectId 科目主键
     * @return 科目
     */
    @Override
    public Subject selectSubjectBySubjectId(String subjectId)
    {
        return subjectMapper.selectSubjectBySubjectId(subjectId);
    }

    /**
     * 查询科目列表
     * 
     * @param subject 科目
     * @return 科目
     */
    @Override
    public List<Subject> selectSubjectList(Subject subject)
    {
        return subjectMapper.selectSubjectList(subject);
    }

    /**
     * 新增科目
     * 
     * @param subject 科目
     * @return 结果
     */
    @Override
    public int insertSubject(Subject subject)
    {
        return subjectMapper.insertSubject(subject);
    }

    /**
     * 修改科目
     * 
     * @param subject 科目
     * @return 结果
     */
    @Override
    public int updateSubject(Subject subject)
    {
        return subjectMapper.updateSubject(subject);
    }

    /**
     * 批量删除科目
     * 
     * @param subjectIds 需要删除的科目主键
     * @return 结果
     */
    @Override
    public int deleteSubjectBySubjectIds(String[] subjectIds)
    {
        return subjectMapper.deleteSubjectBySubjectIds(subjectIds);
    }

    /**
     * 删除科目信息
     * 
     * @param subjectId 科目主键
     * @return 结果
     */
    @Override
    public int deleteSubjectBySubjectId(String subjectId)
    {
        return subjectMapper.deleteSubjectBySubjectId(subjectId);
    }
}

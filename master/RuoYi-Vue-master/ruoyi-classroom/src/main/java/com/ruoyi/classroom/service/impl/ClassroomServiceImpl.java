package com.ruoyi.classroom.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.classroom.mapper.ClassroomMapper;
import com.ruoyi.classroom.domain.Classroom;
import com.ruoyi.classroom.service.IClassroomService;

/**
 * 教室Service业务层处理
 */
@Service
public class ClassroomServiceImpl implements IClassroomService 
{
    @Autowired
    private ClassroomMapper classroomMapper;

    /**
     * 查询教室
     * 
     * @param classroomId 教室主键
     * @return 教室
     */
    @Override
    public Classroom selectClassroomByClassroomId(String classroomId)
    {
        return classroomMapper.selectClassroomByClassroomId(classroomId);
    }

    /**
     * 查询教室列表
     * 
     * @param classroom 教室
     * @return 教室
     */
    @Override
    public List<Classroom> selectClassroomList(Classroom classroom)
    {
        return classroomMapper.selectClassroomList(classroom);
    }

    /**
     * 新增教室
     * 
     * @param classroom 教室
     * @return 结果
     */
    @Override
    public int insertClassroom(Classroom classroom)
    {
        return classroomMapper.insertClassroom(classroom);
    }

    /**
     * 修改教室
     * 
     * @param classroom 教室
     * @return 结果
     */
    @Override
    public int updateClassroom(Classroom classroom)
    {
        return classroomMapper.updateClassroom(classroom);
    }

    /**
     * 批量删除教室
     * 
     * @param classroomIds 需要删除的教室主键
     * @return 结果
     */
    @Override
    public int deleteClassroomByClassroomIds(String[] classroomIds)
    {
        return classroomMapper.deleteClassroomByClassroomIds(classroomIds);
    }

    /**
     * 删除教室信息
     * 
     * @param classroomId 教室主键
     * @return 结果
     */
    @Override
    public int deleteClassroomByClassroomId(String classroomId)
    {
        return classroomMapper.deleteClassroomByClassroomId(classroomId);
    }
}

package com.ruoyi.classroom.service;

import java.util.List;
import com.ruoyi.classroom.domain.Classroom;

/**
 * 教室Service接口
 */
public interface IClassroomService 
{
    /**
     * 查询教室
     * 
     * @param classroomId 教室主键
     * @return 教室
     */
    public Classroom selectClassroomByClassroomId(String classroomId);

    /**
     * 查询教室列表
     * 
     * @param classroom 教室
     * @return 教室集合
     */
    public List<Classroom> selectClassroomList(Classroom classroom);

    /**
     * 新增教室
     * 
     * @param classroom 教室
     * @return 结果
     */
    public int insertClassroom(Classroom classroom);

    /**
     * 修改教室
     * 
     * @param classroom 教室
     * @return 结果
     */
    public int updateClassroom(Classroom classroom);

    /**
     * 批量删除教室
     * 
     * @param classroomIds 需要删除的教室主键集合
     * @return 结果
     */
    public int deleteClassroomByClassroomIds(String[] classroomIds);

    /**
     * 删除教室信息
     * 
     * @param classroomId 教室主键
     * @return 结果
     */
    public int deleteClassroomByClassroomId(String classroomId);
}

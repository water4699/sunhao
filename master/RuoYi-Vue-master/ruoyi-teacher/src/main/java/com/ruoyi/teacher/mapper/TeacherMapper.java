package com.ruoyi.teacher.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.teacher.domain.Teacher;

/**
 * 教师信息Mapper接口
 */
public interface TeacherMapper 
{
    /**
     * 查询教师信息
     * 
     * @param teacherId 教师信息主键
     * @return 教师信息
     */
    public Teacher selectTeacherByTeacherId(String teacherId);

    /**
     * 查询教师信息列表
     * 
     * @param teacher 教师信息
     * @return 教师信息集合
     */
    public List<Teacher> selectTeacherList(Teacher teacher);

    /**
     * 新增教师信息
     * 
     * @param teacher 教师信息
     * @return 结果
     */
    public int insertTeacher(Teacher teacher);

    /**
     * 修改教师信息
     * 
     * @param teacher 教师信息
     * @return 结果
     */
    public int updateTeacher(Teacher teacher);

    /**
     * 删除教师信息
     * 
     * @param teacherId 教师信息主键
     * @return 结果
     */
    public int deleteTeacherByTeacherId(String teacherId);

    /**
     * 批量删除教师信息
     * 
     * @param teacherIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTeacherByTeacherIds(String[] teacherIds);

    String selectSubjectBySubjectId(Integer subjectid);

    int countTeacherByUserId(@Param("userId") String userId);

    Teacher selectTeacherByUserId(@Param("userId") String userId);
}

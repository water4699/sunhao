package com.ruoyi.schedule.service;

import java.util.List;
import com.ruoyi.schedule.domain.ClassroomSchedule;

/**
 * 教室时间Service接口
 */
public interface IClassroomScheduleService 
{
    /**
     * 查询教室时间
     * 
     * @param scheduleId 教室时间主键
     * @return 教室时间
     */
    public ClassroomSchedule selectClassroomScheduleByScheduleId(String scheduleId);

    /**
     * 查询教室时间列表
     * 
     * @param classroomSchedule 教室时间
     * @return 教室时间集合
     */
    public List<ClassroomSchedule> selectClassroomScheduleList(ClassroomSchedule classroomSchedule);

    /**
     * 新增教室时间
     * 
     * @param classroomSchedule 教室时间
     * @return 结果
     */
    public int insertClassroomSchedule(ClassroomSchedule classroomSchedule);

    /**
     * 修改教室时间
     * 
     * @param classroomSchedule 教室时间
     * @return 结果
     */
    public int updateClassroomSchedule(ClassroomSchedule classroomSchedule);

    /**
     * 批量删除教室时间
     * 
     * @param scheduleIds 需要删除的教室时间主键集合
     * @return 结果
     */
    public int deleteClassroomScheduleByScheduleIds(String[] scheduleIds);

    /**
     * 删除教室时间信息
     * 
     * @param scheduleId 教室时间主键
     * @return 结果
     */
    public int deleteClassroomScheduleByScheduleId(String scheduleId);
}

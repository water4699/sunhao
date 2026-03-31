package com.ruoyi.schedule.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.schedule.mapper.ClassroomScheduleMapper;
import com.ruoyi.schedule.domain.ClassroomSchedule;
import com.ruoyi.schedule.service.IClassroomScheduleService;

/**
 * 教室时间Service业务层处理
 */
@Service
public class ClassroomScheduleServiceImpl implements IClassroomScheduleService 
{
    @Autowired
    private ClassroomScheduleMapper classroomScheduleMapper;

    /**
     * 查询教室时间
     * 
     * @param scheduleId 教室时间主键
     * @return 教室时间
     */
    @Override
    public ClassroomSchedule selectClassroomScheduleByScheduleId(String scheduleId)
    {
        return classroomScheduleMapper.selectClassroomScheduleByScheduleId(scheduleId);
    }

    /**
     * 查询教室时间列表
     * 
     * @param classroomSchedule 教室时间
     * @return 教室时间
     */
    @Override
    public List<ClassroomSchedule> selectClassroomScheduleList(ClassroomSchedule classroomSchedule)
    {
        return classroomScheduleMapper.selectClassroomScheduleList(classroomSchedule);
    }

    /**
     * 新增教室时间
     * 
     * @param classroomSchedule 教室时间
     * @return 结果
     */
    @Override
    public int insertClassroomSchedule(ClassroomSchedule classroomSchedule)
    {
        return classroomScheduleMapper.insertClassroomSchedule(classroomSchedule);
    }

    /**
     * 修改教室时间
     * 
     * @param classroomSchedule 教室时间
     * @return 结果
     */
    @Override
    public int updateClassroomSchedule(ClassroomSchedule classroomSchedule)
    {
        return classroomScheduleMapper.updateClassroomSchedule(classroomSchedule);
    }

    /**
     * 批量删除教室时间
     * 
     * @param scheduleIds 需要删除的教室时间主键
     * @return 结果
     */
    @Override
    public int deleteClassroomScheduleByScheduleIds(String[] scheduleIds)
    {
        return classroomScheduleMapper.deleteClassroomScheduleByScheduleIds(scheduleIds);
    }

    /**
     * 删除教室时间信息
     * 
     * @param scheduleId 教室时间主键
     * @return 结果
     */
    @Override
    public int deleteClassroomScheduleByScheduleId(String scheduleId)
    {
        return classroomScheduleMapper.deleteClassroomScheduleByScheduleId(scheduleId);
    }
}

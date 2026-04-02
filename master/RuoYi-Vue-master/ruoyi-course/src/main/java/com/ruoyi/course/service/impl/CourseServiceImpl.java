package com.ruoyi.course.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.course.mapper.CourseMapper;
import com.ruoyi.course.domain.Course;
import com.ruoyi.course.service.ICourseService;
import com.ruoyi.teacher.domain.Teacher;
import com.ruoyi.teacher.service.ITeacherService;

/**
 * 课程管理Service业务层处理
 */
@Service
public class CourseServiceImpl implements ICourseService 
{
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ITeacherService teacherService;

    /**
     * 查询课程管理
     * 
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    @Override
    public Course selectCourseByCourseId(String courseId)
    {
        return courseMapper.selectCourseByCourseId(courseId);
    }

    /**
     * 查询课程管理列表
     * 
     * @param course 课程管理
     * @return 课程管理
     */
    @Override
    public List<Course> selectCourseList(Course course)
    {
        return courseMapper.selectCourseList(course);
    }

    /**
     * 新增课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    @Override
    public int insertCourse(Course course)
    {
        fillGradeIdIfMissing(course);
        validateNoDuplicateBooking(course);
        return courseMapper.insertCourse(course);
    }

    /**
     * 库表 grade_id 常 NOT NULL；小程序预约可能不传，则从教师档案带出。
     * 仍为空时用 "1" 占位（请保证 grade_level 存在 id=1，或按库改为实际默认年级）。
     */
    private void fillGradeIdIfMissing(Course course)
    {
        if (course == null || StringUtils.isNotEmpty(course.getGradeId()))
        {
            return;
        }
        if (StringUtils.isNotEmpty(course.getTeacherId()))
        {
            Teacher t = teacherService.selectTeacherByTeacherId(course.getTeacherId());
            if (t != null && StringUtils.isNotEmpty(t.getGradeId()))
            {
                course.setGradeId(t.getGradeId());
            }
        }
        if (StringUtils.isEmpty(course.getGradeId()))
        {
            course.setGradeId("1");
        }
    }

    private void validateNoDuplicateBooking(Course course)
    {
        if (course == null || StringUtils.isEmpty(course.getStudentId())
            || StringUtils.isEmpty(course.getTeacherId()) || course.getStartDate() == null)
        {
            return;
        }
        int n = courseMapper.countActiveBookingSameSlot(course);
        if (n > 0)
        {
            throw new ServiceException("您已在该日期预约过这位老师，请勿重复预约");
        }
    }

    /**
     * 修改课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    @Override
    public int updateCourse(Course course)
    {
        return courseMapper.updateCourse(course);
    }

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseByCourseIds(String[] courseIds)
    {
        return courseMapper.deleteCourseByCourseIds(courseIds);
    }

    /**
     * 删除课程管理信息
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseByCourseId(String courseId)
    {
        return courseMapper.deleteCourseByCourseId(courseId);
    }
}

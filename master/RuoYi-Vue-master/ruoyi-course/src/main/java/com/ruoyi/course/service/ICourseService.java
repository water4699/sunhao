package com.ruoyi.course.service;

import java.util.List;
import com.ruoyi.course.domain.Course;
import com.ruoyi.course.domain.TeacherPublishedCourse;

/**
 * 课程管理Service接口
 */
public interface ICourseService 
{
    /**
     * 查询课程管理
     * 
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    public Course selectCourseByCourseId(String courseId);

    /**
     * 查询课程管理列表
     * 
     * @param course 课程管理
     * @return 课程管理集合
     */
    public List<Course> selectCourseList(Course course);

    /**
     * 新增课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    public int insertCourse(Course course);

    /**
     * 老师上架课程（无 studentId）
     *
     * @param course 课程
     * @return 结果
     */
    public int insertTeacherPublishedCourse(TeacherPublishedCourse course);

    public java.util.List<TeacherPublishedCourse> selectTeacherPublishedCourseList(TeacherPublishedCourse query);

    public TeacherPublishedCourse selectTeacherPublishedCourseById(String publishId);

    public int updateTeacherPublishedCourse(TeacherPublishedCourse course);

    public int updateTeacherPublishedCourseStatus(TeacherPublishedCourse course);

    public int deleteTeacherPublishedCourse(TeacherPublishedCourse course);

    /**
     * 修改课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的课程管理主键集合
     * @return 结果
     */
    public int deleteCourseByCourseIds(String[] courseIds);

    /**
     * 删除课程管理信息
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    public int deleteCourseByCourseId(String courseId);

    /**
     * 教师端：预约请求列表
     */
    public List<Course> selectTeacherBookingAppList(String teacherId, Long status);

    /**
     * 教师端：处理预约（1同意/2拒绝）
     */
    public int teacherDecideBooking(String teacherId, String courseId, Long status);

    /**
     * 学生端：待确认时取消，已确认时申请取消。
     */
    public int studentCancelBooking(String studentId, String courseId, String cancelReason);
}

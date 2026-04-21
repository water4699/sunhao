package com.ruoyi.course.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.course.domain.Course;

/**
 * 课程管理Mapper接口
 */
public interface CourseMapper 
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
     * 修改课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 删除课程管理
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    public int deleteCourseByCourseId(String courseId);

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseByCourseIds(String[] courseIds);

    /**
     * 同一学生、教师、上课日期的有效预约数量（排除已取消 status=2）
     */
    public int countActiveBookingSameSlot(Course course);

    /**
     * 教师端：预约请求列表
     */
    public List<Course> selectTeacherBookingAppList(@Param("teacherId") String teacherId, @Param("status") Long status);

    /**
     * 教师端：处理预约（1同意/2拒绝）
     */
    public int updateBookingStatusByTeacher(@Param("courseId") String courseId, @Param("teacherId") String teacherId, @Param("status") Long status);

    /**
     * 学生端：取消预约/申请取消。
     */
    public int updateBookingStatusByStudent(@Param("courseId") String courseId, @Param("studentId") String studentId, @Param("status") Long status, @Param("cancelReason") String cancelReason);
}

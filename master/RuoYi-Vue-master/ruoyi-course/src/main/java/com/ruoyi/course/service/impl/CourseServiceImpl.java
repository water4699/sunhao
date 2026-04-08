package com.ruoyi.course.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.course.mapper.CourseMapper;
import com.ruoyi.course.domain.Course;
import com.ruoyi.course.domain.TeacherPublishedCourse;
import com.ruoyi.course.mapper.TeacherPublishedCourseMapper;
import com.ruoyi.course.service.ICourseService;
import com.ruoyi.student.domain.Student;
import com.ruoyi.student.service.IStudentService;
import com.ruoyi.teacher.domain.Teacher;
import com.ruoyi.teacher.service.ITeacherService;
import com.ruoyi.users.domain.Users;
import com.ruoyi.users.service.IUsersService;

/**
 * 课程管理Service业务层处理
 */
@Service
public class CourseServiceImpl implements ICourseService 
{
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherPublishedCourseMapper teacherPublishedCourseMapper;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IUsersService usersService;

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
        resolveStudentIdForCourse(course);
        fillGradeIdIfMissing(course);
        validateNoDuplicateBooking(course);
        return courseMapper.insertCourse(course);
    }

    @Override
    public int insertTeacherPublishedCourse(TeacherPublishedCourse course)
    {
        if (StringUtils.isEmpty(course.getTeacherId()))
        {
            throw new ServiceException("老师信息不能为空");
        }
        if (StringUtils.isEmpty(course.getSubjectId()))
        {
            throw new ServiceException("请选择科目");
        }
        if (StringUtils.isEmpty(course.getGradeId()))
        {
            course.setGradeId("1");
        }
        if (course.getStatus() == null)
        {
            course.setStatus(0L);
        }
        if (StringUtils.isEmpty(course.getExpectedHours()))
        {
            course.setExpectedHours("1");
        }
        if (course.getCreatedAt() == null)
        {
            course.setCreatedAt(DateUtils.getNowDate());
        }
        return teacherPublishedCourseMapper.insertTeacherPublishedCourse(course);
    }

    @Override
    public List<TeacherPublishedCourse> selectTeacherPublishedCourseList(TeacherPublishedCourse query)
    {
        return teacherPublishedCourseMapper.selectTeacherPublishedCourseList(query);
    }

    @Override
    public TeacherPublishedCourse selectTeacherPublishedCourseById(String publishId)
    {
        return teacherPublishedCourseMapper.selectTeacherPublishedCourseById(publishId);
    }

    @Override
    public int updateTeacherPublishedCourse(TeacherPublishedCourse course)
    {
        if (course == null || StringUtils.isEmpty(course.getPublishId()) || StringUtils.isEmpty(course.getTeacherId()))
        {
            throw new ServiceException("发布信息参数不完整");
        }
        if (StringUtils.isEmpty(course.getSubjectId()))
        {
            throw new ServiceException("请选择科目");
        }
        if (StringUtils.isEmpty(course.getGradeId()))
        {
            throw new ServiceException("请选择年级");
        }
        if (course.getStartDate() == null)
        {
            throw new ServiceException("请选择可授课日期");
        }
        if (StringUtils.isEmpty(course.getAddress()))
        {
            throw new ServiceException("请输入家教说明或授课地址");
        }
        if (course.getHourlyRate() == null)
        {
            throw new ServiceException("请输入课时费用");
        }
        return teacherPublishedCourseMapper.updateTeacherPublishedCourse(course);
    }

    @Override
    public int updateTeacherPublishedCourseStatus(TeacherPublishedCourse course)
    {
        if (course == null || StringUtils.isEmpty(course.getPublishId()) || StringUtils.isEmpty(course.getTeacherId()) || course.getStatus() == null)
        {
            throw new ServiceException("发布状态参数不完整");
        }
        if (course.getStatus() != 0L && course.getStatus() != 1L)
        {
            throw new ServiceException("仅支持上架(0)或下架(1)");
        }
        return teacherPublishedCourseMapper.updateTeacherPublishedCourseStatus(course);
    }

    @Override
    public int deleteTeacherPublishedCourse(TeacherPublishedCourse course)
    {
        if (course == null || StringUtils.isEmpty(course.getPublishId()) || StringUtils.isEmpty(course.getTeacherId()))
        {
            throw new ServiceException("发布信息参数不完整");
        }
        return teacherPublishedCourseMapper.deleteTeacherPublishedCourse(course);
    }

    /**
     * course.student_id 外键指向 student.student_id；小程序常传 users.users_id。
     * 先按 student_id 命中则不变；否则按 user_id 找档案；再无则对学员/家长用户自动补一条 student。
     */
    private void resolveStudentIdForCourse(Course course)
    {
        if (course == null || StringUtils.isEmpty(course.getStudentId()))
        {
            throw new ServiceException("学员信息不能为空");
        }
        String key = course.getStudentId().trim();
        if (studentService.selectStudentByStudentId(key) != null)
        {
            course.setStudentId(key);
            return;
        }
        Student byUser = studentService.selectStudentByUserId(key);
        if (byUser != null && StringUtils.isNotEmpty(byUser.getStudentId()))
        {
            course.setStudentId(byUser.getStudentId());
            return;
        }
        Users u = usersService.selectUsersByUsersId(key);
        if (u == null)
        {
            throw new ServiceException("学员账号不存在，无法预约。请使用已注册的学生或家长账号。");
        }
        if ("teacher".equalsIgnoreCase(StringUtils.trim(u.getUsersType())))
        {
            throw new ServiceException("教师账号不能作为学员预约课程，请使用学生或家长账号。");
        }
        Student row = new Student();
        row.setUserId(key);
        row.setStatus(0L);
        row.setCreatedAt(DateUtils.getNowDate());
        // 库表 subject_id 常 NOT NULL；约课请求里一般有 subjectId，否则用 1 占位（须与 subject 表数据一致）
        if (course != null && StringUtils.isNotEmpty(course.getSubjectId()))
        {
            row.setSubjectId(course.getSubjectId().trim());
        }
        else
        {
            row.setSubjectId("1");
        }
        // sunhao-project.sql：real_name / school / grade_level 均为 NOT NULL 且无默认值
        String rn = StringUtils.isNotEmpty(u.getUsersname()) ? u.getUsersname().trim() : "学员";
        row.setRealName(rn);
        row.setSchool("待完善");
        row.setGradeLevel("待完善");
        if (studentService.insertStudent(row) <= 0 || StringUtils.isEmpty(row.getStudentId()))
        {
            throw new ServiceException("创建学员档案失败，请稍后重试或联系管理员。");
        }
        course.setStudentId(row.getStudentId());
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


    @Override
    public List<Course> selectTeacherBookingAppList(String teacherId, Long status)
    {
        if (StringUtils.isEmpty(teacherId))
        {
            return java.util.Collections.emptyList();
        }
        return courseMapper.selectTeacherBookingAppList(teacherId, status);
    }

    @Override
    public int teacherDecideBooking(String teacherId, String courseId, Long status)
    {
        if (StringUtils.isEmpty(teacherId) || StringUtils.isEmpty(courseId))
        {
            throw new ServiceException("参数不能为空");
        }
        if (status == null || (status != 1L && status != 2L))
        {
            throw new ServiceException("仅支持同意(1)或拒绝(2)");
        }
        Course existing = courseMapper.selectCourseByCourseId(courseId);
        if (existing == null)
        {
            throw new ServiceException("预约记录不存在");
        }
        if (!teacherId.equals(existing.getTeacherId()))
        {
            throw new ServiceException("无权处理该预约");
        }
        Long current = existing.getStatus();
        if (current != null && current == 3L)
        {
            if (status != 2L)
            {
                throw new ServiceException("取消申请仅支持同意取消");
            }
            return courseMapper.updateBookingStatusByTeacher(courseId, teacherId, status);
        }
        if (current != null && current != 0L)
        {
            throw new ServiceException("该预约已处理，请勿重复操作");
        }
        return courseMapper.updateBookingStatusByTeacher(courseId, teacherId, status);
    }

    @Override
    public int studentCancelBooking(String studentId, String courseId, String cancelReason)
    {
        if (StringUtils.isEmpty(studentId) || StringUtils.isEmpty(courseId))
        {
            throw new ServiceException("参数不能为空");
        }
        Course existing = courseMapper.selectCourseByCourseId(courseId);
        if (existing == null)
        {
            throw new ServiceException("预约记录不存在");
        }
        if (!studentId.equals(existing.getStudentId()))
        {
            throw new ServiceException("只能取消自己的预约");
        }
        String reason = StringUtils.isEmpty(cancelReason) ? "学生主动取消" : cancelReason.trim();
        if (reason.length() > 200)
        {
            throw new ServiceException("取消原因不能超过200字");
        }
        Long current = existing.getStatus();
        if (current == null || current == 0L)
        {
            return courseMapper.updateBookingStatusByStudent(courseId, studentId, 2L, reason);
        }
        if (current == 1L)
        {
            return courseMapper.updateBookingStatusByStudent(courseId, studentId, 3L, reason);
        }
        if (current == 3L)
        {
            throw new ServiceException("已提交取消申请，请等待老师处理");
        }
        throw new ServiceException("当前状态不可取消");
    }

}

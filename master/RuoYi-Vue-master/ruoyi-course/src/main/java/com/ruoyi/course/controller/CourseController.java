package com.ruoyi.course.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.course.domain.Course;
import com.ruoyi.course.domain.TeacherPublishedCourse;
import com.ruoyi.course.service.ICourseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.student.domain.Student;
import com.ruoyi.student.service.IStudentService;
import com.ruoyi.teacher.domain.Teacher;
import com.ruoyi.teacher.service.ITeacherService;

/**
 * 课程管理Controller
 */
@RestController
@RequestMapping("/system/course")
public class CourseController extends BaseController
{
    @Autowired
    private ICourseService courseService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private ITeacherService teacherService;

    private Teacher currentApprovedTeacherOrThrow()
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu == null || !lu.isBusinessUser())
        {
            throw new RuntimeException("请先登录");
        }
        if (!"teacher".equalsIgnoreCase(lu.getBusinessUsersType()))
        {
            throw new RuntimeException("仅老师账号可操作");
        }
        Teacher teacher = teacherService.selectTeacherByUserId(String.valueOf(lu.getUserId()));
        if (teacher == null)
        {
            throw new RuntimeException("请先提交老师入驻申请");
        }
        if (teacher.getStatus() == null || teacher.getStatus() != 1L)
        {
            throw new RuntimeException("入驻申请审核通过后才可操作");
        }
        return teacher;
    }

    /** 业务端 users 登录 JWT 中带 *:*:*，列表/导出必须按本人 student 维度收口，避免横向越权 */
    private String businessStudentIdOrNull()
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu == null || !lu.isBusinessUser())
        {
            return null;
        }
        Student st = studentService.selectStudentByUserId(String.valueOf(lu.getUserId()));
        if (st == null || StringUtils.isEmpty(st.getStudentId()))
        {
            return "";
        }
        return st.getStudentId();
    }

    /**
     * 查询课程管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(Course course)
    {
        String sid = businessStudentIdOrNull();
        if (sid != null)
        {
            if (StringUtils.isEmpty(sid))
            {
                return getDataTable(Collections.emptyList());
            }
            course.setStudentId(sid);
        }
        startPage();
        List<Course> list = courseService.selectCourseList(course);
        return getDataTable(list);
    }

    /**
     * 导出课程管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:export')")
    @Log(title = "课程管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Course course)
    {
        String sid = businessStudentIdOrNull();
        List<Course> list;
        if (sid != null && StringUtils.isEmpty(sid))
        {
            list = Collections.emptyList();
        }
        else
        {
            if (sid != null)
            {
                course.setStudentId(sid);
            }
            list = courseService.selectCourseList(course);
        }
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        util.exportExcel(response, list, "课程管理数据");
    }

    /**
     * 获取课程管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:course:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") String courseId)
    {
        Course row = courseService.selectCourseByCourseId(courseId);
        if (row == null)
        {
            return error("记录不存在");
        }
        String sid = businessStudentIdOrNull();
        if (sid != null && (StringUtils.isEmpty(sid) || !sid.equals(row.getStudentId())))
        {
            return error("无权查看该预约");
        }
        return success(row);
    }

    /**
     * 新增课程管理
     */
    @PreAuthorize("@ss.hasPermi('system:course:add')")
    @Log(title = "课程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Course course)
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu != null && lu.isBusinessUser())
        {
            try
            {
                // 业务侧 course.student_id 需要落到 student.student_id（而不是 users_id）
                String sid = businessStudentIdOrNull();
                // 无 student 档案时，先传 users_id，交给 Service 自动补建 student 档案
                if (StringUtils.isEmpty(sid))
                {
                    sid = String.valueOf(lu.getUserId());
                }
                course.setStudentId(sid);
            }
            catch (Exception e)
            {
                return error("请先登录后再预约");
            }
        }
        else if (StringUtils.isEmpty(course.getStudentId()))
        {
            try
            {
                course.setStudentId(String.valueOf(SecurityUtils.getUserId()));
            }
            catch (Exception e)
            {
                return error("请先登录后再预约");
            }
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
            course.setCreatedAt(new Date());
        }
        return toAjax(courseService.insertCourse(course));
    }

    /**
     * 小程序老师：上架课程（仅教师且入驻审核通过可用）
     */
    @PostMapping("/app/publish")
    public AjaxResult appPublish(@RequestBody Course course)
    {
        Teacher teacher;
        try
        {
            teacher = currentApprovedTeacherOrThrow();
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
        if (course.getStartDate() == null)
        {
            return error("请选择开课日期");
        }
        if (StringUtils.isEmpty(course.getAddress()))
        {
            return error("请输入上课地址或课程说明");
        }
        course.setTeacherId(teacher.getTeacherId());
        course.setStudentId(null);
        if (StringUtils.isEmpty(course.getSubjectId()) && teacher.getSubjectId() != null)
        {
            course.setSubjectId(String.valueOf(teacher.getSubjectId()));
        }
        if (course.getHourlyRate() == null)
        {
            course.setHourlyRate(teacher.getHourlyRate());
        }
        if (course.getHourlyRate() == null)
        {
            return error("请输入课时费用");
        }
        if (StringUtils.isEmpty(course.getGradeId()))
        {
            course.setGradeId(teacher.getGradeId());
        }
        TeacherPublishedCourse published = new TeacherPublishedCourse();
        published.setTeacherId(course.getTeacherId());
        published.setSubjectId(course.getSubjectId());
        published.setGradeId(course.getGradeId());
        published.setStatus(0L);
        published.setStartDate(course.getStartDate());
        published.setAddress(course.getAddress());
        published.setExpectedHours(course.getExpectedHours());
        published.setHourlyRate(course.getHourlyRate());
        published.setCreatedAt(course.getCreatedAt());
        return toAjax(courseService.insertTeacherPublishedCourse(published));
    }

    @GetMapping("/app/my-published/list")
    public TableDataInfo appMyPublishedList(TeacherPublishedCourse query)
    {
        Teacher teacher;
        try
        {
            teacher = currentApprovedTeacherOrThrow();
        }
        catch (RuntimeException e)
        {
            return getDataTable(Collections.emptyList());
        }
        if (query == null)
        {
            query = new TeacherPublishedCourse();
        }
        query.setTeacherId(teacher.getTeacherId());
        startPage();
        List<TeacherPublishedCourse> list = courseService.selectTeacherPublishedCourseList(query);
        return getDataTable(list);
    }

    @PutMapping("/app/my-published/{publishId}")
    public AjaxResult appUpdateMyPublished(@PathVariable("publishId") String publishId, @RequestBody TeacherPublishedCourse body)
    {
        Teacher teacher;
        try
        {
            teacher = currentApprovedTeacherOrThrow();
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
        if (body == null)
        {
            return error("参数不能为空");
        }
        body.setPublishId(publishId);
        body.setTeacherId(teacher.getTeacherId());
        return toAjax(courseService.updateTeacherPublishedCourse(body));
    }

    @PostMapping("/app/my-published/{publishId}/status")
    public AjaxResult appUpdateMyPublishedStatus(@PathVariable("publishId") String publishId, @RequestBody TeacherPublishedCourse body)
    {
        Teacher teacher;
        try
        {
            teacher = currentApprovedTeacherOrThrow();
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
        TeacherPublishedCourse row = new TeacherPublishedCourse();
        row.setPublishId(publishId);
        row.setTeacherId(teacher.getTeacherId());
        row.setStatus(body == null ? null : body.getStatus());
        return toAjax(courseService.updateTeacherPublishedCourseStatus(row));
    }

    @DeleteMapping("/app/my-published/{publishId}")
    public AjaxResult appDeleteMyPublished(@PathVariable("publishId") String publishId)
    {
        Teacher teacher;
        try
        {
            teacher = currentApprovedTeacherOrThrow();
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
        TeacherPublishedCourse row = new TeacherPublishedCourse();
        row.setPublishId(publishId);
        row.setTeacherId(teacher.getTeacherId());
        return toAjax(courseService.deleteTeacherPublishedCourse(row));
    }

    /**
     * 小程序：老师已发布课程列表（默认只查上架中 status=0）
     */
    @GetMapping("/app/published/list")
    public TableDataInfo appPublishedList(TeacherPublishedCourse query)
    {
        if (query == null)
        {
            query = new TeacherPublishedCourse();
        }
        if (query.getStatus() == null)
        {
            query.setStatus(0L);
        }
        startPage();
        List<TeacherPublishedCourse> list = courseService.selectTeacherPublishedCourseList(query);
        return getDataTable(list);
    }

    /**
     * 小程序：老师已发布课程详情
     */
    @GetMapping("/app/published/{publishId}")
    public AjaxResult appPublishedDetail(@PathVariable("publishId") String publishId)
    {
        TeacherPublishedCourse row = courseService.selectTeacherPublishedCourseById(publishId);
        if (row == null)
        {
            return error("课程不存在");
        }
        return success(row);
    }



    /**
     * 小程序老师：查看预约请求（status: 0待处理 1已同意 2已拒绝，不传则全部）
     */
    @GetMapping("/app/teacher/booking/list")
    public TableDataInfo appTeacherBookingList(Long status)
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu == null || !lu.isBusinessUser())
        {
            return getDataTable(Collections.emptyList());
        }
        if (!"teacher".equalsIgnoreCase(lu.getBusinessUsersType()))
        {
            return getDataTable(Collections.emptyList());
        }
        Teacher teacher = teacherService.selectTeacherByUserId(String.valueOf(lu.getUserId()));
        if (teacher == null || StringUtils.isEmpty(teacher.getTeacherId()))
        {
            return getDataTable(Collections.emptyList());
        }
        startPage();
        List<Course> list = courseService.selectTeacherBookingAppList(teacher.getTeacherId(), status);
        return getDataTable(list);
    }

    /**
     * 小程序老师：处理预约（status=1 同意，status=2 拒绝）
     */
    @PostMapping("/app/teacher/booking/{courseId}/decision")
    public AjaxResult appTeacherBookingDecision(@PathVariable("courseId") String courseId, @RequestBody Course body)
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu == null || !lu.isBusinessUser())
        {
            return error("请先登录");
        }
        if (!"teacher".equalsIgnoreCase(lu.getBusinessUsersType()))
        {
            return error("仅老师可处理预约");
        }
        Teacher teacher = teacherService.selectTeacherByUserId(String.valueOf(lu.getUserId()));
        if (teacher == null || StringUtils.isEmpty(teacher.getTeacherId()))
        {
            return error("未找到老师档案");
        }
        Long status = body == null ? null : body.getStatus();
        return toAjax(courseService.teacherDecideBooking(teacher.getTeacherId(), courseId, status));
    }

    @PostMapping("/app/booking/{courseId}/cancel")
    public AjaxResult appStudentCancelBooking(@PathVariable("courseId") String courseId, @RequestBody(required = false) Course body)
    {
        String sid = businessStudentIdOrNull();
        if (sid == null || StringUtils.isEmpty(sid))
        {
            return error("请先登录学生或家长账号");
        }
        return toAjax(courseService.studentCancelBooking(sid, courseId, body == null ? null : body.getCancelReason()));
    }

    /**
     * 修改课程管理
     */
    @PreAuthorize("@ss.hasPermi('system:course:edit')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Course course)
    {
        String sid = businessStudentIdOrNull();
        if (sid != null)
        {
            if (StringUtils.isEmpty(sid) || StringUtils.isEmpty(course.getCourseId()))
            {
                return error("无权修改该预约");
            }
            Course existing = courseService.selectCourseByCourseId(course.getCourseId());
            if (existing == null || !sid.equals(existing.getStudentId()))
            {
                return error("无权修改该预约");
            }
            course.setStudentId(sid);
        }
        return toAjax(courseService.updateCourse(course));
    }

    /**
     * 删除课程管理
     */
    @PreAuthorize("@ss.hasPermi('system:course:remove')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable String[] courseIds)
    {
        String sid = businessStudentIdOrNull();
        if (sid != null)
        {
            if (StringUtils.isEmpty(sid))
            {
                return error("无权删除预约");
            }
            for (String courseId : courseIds)
            {
                Course existing = courseService.selectCourseByCourseId(courseId);
                if (existing == null || !sid.equals(existing.getStudentId()))
                {
                    return error("只能取消本人的预约");
                }
            }
        }
        return toAjax(courseService.deleteCourseByCourseIds(courseIds));
    }
}

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
import com.ruoyi.course.service.ICourseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.student.domain.Student;
import com.ruoyi.student.service.IStudentService;

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
                startPage();
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
                course.setStudentId(String.valueOf(SecurityUtils.getUserId()));
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

package com.ruoyi.teacher.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.notification.domain.Notification;
import com.ruoyi.notification.service.INotificationService;
import com.ruoyi.teacher.domain.TeacherVo;
import org.springframework.transaction.annotation.Transactional;
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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.teacher.domain.Teacher;
import com.ruoyi.teacher.service.ITeacherService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教师信息Controller
 */
@RestController
@RequestMapping("/system/teacher")
public class TeacherController extends BaseController
{
    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private INotificationService notificationService;

    /**
     * 查询教师信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:teacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(Teacher teacher)
    {
        startPage();
        List<Teacher> teacherList = teacherService.selectTeacherList(teacher);
        List<TeacherVo> list = teacherList.stream().map(tecaher -> {
            TeacherVo teacherVo = new TeacherVo();
            BeanUtils.copyBeanProp(teacherVo, tecaher);
            teacherVo.setSubjectName(teacherService.selectSubjectBySubjectId(tecaher.getSubjectId()));
            return teacherVo;
        }).collect(Collectors.toList());
        return getDataTable(list);
    }

    /**
     * 导出教师信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:teacher:export')")
    @Log(title = "教师信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Teacher teacher)
    {
        List<Teacher> list = teacherService.selectTeacherList(teacher);
        ExcelUtil<Teacher> util = new ExcelUtil<Teacher>(Teacher.class);
        util.exportExcel(response, list, "教师信息数据");
    }

    /**
     * 获取教师信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:teacher:query')")
    @GetMapping(value = "/{teacherId}")
    public AjaxResult getInfo(@PathVariable("teacherId") String teacherId)
    {
        Teacher teacher = teacherService.selectTeacherByTeacherId(teacherId);
        TeacherVo teacherVo = new TeacherVo();
        BeanUtils.copyBeanProp(teacherVo, teacher);
        String s = teacherService.selectSubjectBySubjectId(teacher.getSubjectId());
        teacherVo.setSubjectName(s);
        return success(teacherVo);
    }

    /**
     * 新增教师信息
     */
        @PreAuthorize("@ss.hasPermi('system:teacher:add')")
        @Log(title = "教师信息", businessType = BusinessType.INSERT)
        @PostMapping
        public AjaxResult add(@RequestBody Teacher teacher)
        {
            if (StringUtils.isEmpty(teacher.getUserId()))
            {
                try
                {
                    teacher.setUserId(String.valueOf(SecurityUtils.getUserId()));
                }
                catch (Exception e)
                {
                    return error("请先登录后再提交入驻申请");
                }
            }
            if (teacherService.countTeacherByUserId(teacher.getUserId()) > 0)
            {
                return error("该用户已存在教师档案，请勿重复添加；如需变更审核状态请使用「修改」或联系管理员。");
            }
            /* 入驻/新增一律待审核，禁止客户端自拟「已通过」 */
            teacher.setStatus(0L);
            return toAjax(teacherService.insertTeacher(teacher));
        }

    /**
     * 小程序老师入驻（仅需登录，不要求 system:teacher:add；审核仅能在管理端完成）
     */
    @PostMapping("/app/join")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult appJoin(@RequestBody Teacher teacher)
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu == null || !lu.isBusinessUser())
        {
            return error("请先登录后再提交入驻申请");
        }
        if (!"teacher".equalsIgnoreCase(lu.getBusinessUsersType()))
        {
            return error("仅老师账号可申请入驻");
        }
        String uid;
        try
        {
            uid = String.valueOf(SecurityUtils.getUserId());
        }
        catch (Exception e)
        {
            return error("请先登录后再提交入驻申请");
        }
        if (teacherService.countTeacherByUserId(uid) > 0)
        {
            return error("您已提交过入驻申请，请等待管理员审核；请勿重复提交。");
        }
        teacher.setUserId(uid);
        teacher.setStatus(0L);
        int rows = teacherService.insertTeacher(teacher);
        if (rows <= 0)
        {
            return error("提交入驻申请失败，请稍后重试");
        }

        Notification notice = new Notification();
        notice.setTitle("教师入驻申请");
        // notification.type 为枚举：system/booking/payment/promotion
        notice.setType("booking");
        notice.setStatus(0L);
        notice.setSentAt(new Date());
        String realName = StringUtils.isNotEmpty(teacher.getRealName()) ? teacher.getRealName() : "未填写姓名";
        String university = StringUtils.isNotEmpty(teacher.getUniversity()) ? teacher.getUniversity() : "未填写学校";
        notice.setContent("用户ID=" + uid + "，姓名=" + realName + "，学校=" + university + "，提交了教师入驻申请，请审核。");
        notificationService.insertNotification(notice);
        return toAjax(rows);
    }

    /**
     * 小程序：查询当前登录老师的入驻状态
     */
    @GetMapping("/app/status")
    public AjaxResult appStatus()
    {
        String uid;
        try
        {
            uid = String.valueOf(SecurityUtils.getUserId());
        }
        catch (Exception e)
        {
            return error("请先登录");
        }
        Teacher teacher = teacherService.selectTeacherByUserId(uid);
        AjaxResult ajax = AjaxResult.success();
        if (teacher == null)
        {
            ajax.put("hasSubmitted", false);
            ajax.put("approved", false);
            ajax.put("status", -1);
            return ajax;
        }
        long status = teacher.getStatus() == null ? 0L : teacher.getStatus();
        ajax.put("hasSubmitted", true);
        ajax.put("approved", status == 1L);
        ajax.put("status", status);
        ajax.put("teacherId", teacher.getTeacherId());
        ajax.put("realName", teacher.getRealName());
        return ajax;
    }

    /**
     * 修改教师信息
     */
    @PreAuthorize("@ss.hasPermi('system:teacher:edit')")
    @Log(title = "教师信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Teacher teacher)
    {
        return toAjax(teacherService.updateTeacher(teacher));
    }

    /**
     * 删除教师信息
     */
    @PreAuthorize("@ss.hasPermi('system:teacher:remove')")
    @Log(title = "教师信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{teacherIds}")
    public AjaxResult remove(@PathVariable String[] teacherIds)
    {
        return toAjax(teacherService.deleteTeacherByTeacherIds(teacherIds));
    }
}

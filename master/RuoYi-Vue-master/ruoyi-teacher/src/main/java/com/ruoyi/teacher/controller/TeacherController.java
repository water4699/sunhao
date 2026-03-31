package com.ruoyi.teacher.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.teacher.domain.TeacherVo;
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
            System.err.println(teacherVo);
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
            System.err.println(teacher);
            return toAjax(teacherService.insertTeacher(teacher));
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

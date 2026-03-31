package com.ruoyi.classroom.controller;

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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.classroom.domain.Classroom;
import com.ruoyi.classroom.service.IClassroomService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教室Controller
 */
@RestController
@RequestMapping("/system/classroom")
public class ClassroomController extends BaseController
{
    @Autowired
    private IClassroomService classroomService;

    /**
     * 查询教室列表
     */
    @PreAuthorize("@ss.hasPermi('system:classroom:list')")
    @GetMapping("/list")
    public TableDataInfo list(Classroom classroom)
    {
        startPage();
        List<Classroom> list = classroomService.selectClassroomList(classroom);
        return getDataTable(list);
    }

    /**
     * 导出教室列表
     */
    @PreAuthorize("@ss.hasPermi('system:classroom:export')")
    @Log(title = "教室", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Classroom classroom)
    {
        List<Classroom> list = classroomService.selectClassroomList(classroom);
        ExcelUtil<Classroom> util = new ExcelUtil<Classroom>(Classroom.class);
        util.exportExcel(response, list, "教室数据");
    }

    /**
     * 获取教室详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:classroom:query')")
    @GetMapping(value = "/{classroomId}")
    public AjaxResult getInfo(@PathVariable("classroomId") String classroomId)
    {
        return success(classroomService.selectClassroomByClassroomId(classroomId));
    }

    /**
     * 新增教室
     */
    @PreAuthorize("@ss.hasPermi('system:classroom:add')")
    @Log(title = "教室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Classroom classroom)
    {
        return toAjax(classroomService.insertClassroom(classroom));
    }

    /**
     * 修改教室
     */
    @PreAuthorize("@ss.hasPermi('system:classroom:edit')")
    @Log(title = "教室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Classroom classroom)
    {
        return toAjax(classroomService.updateClassroom(classroom));
    }

    /**
     * 删除教室
     */
    @PreAuthorize("@ss.hasPermi('system:classroom:remove')")
    @Log(title = "教室", businessType = BusinessType.DELETE)
	@DeleteMapping("/{classroomIds}")
    public AjaxResult remove(@PathVariable String[] classroomIds)
    {
        return toAjax(classroomService.deleteClassroomByClassroomIds(classroomIds));
    }
}

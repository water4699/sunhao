package com.ruoyi.schedule.controller;

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
import com.ruoyi.schedule.domain.ClassroomSchedule;
import com.ruoyi.schedule.service.IClassroomScheduleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教室时间Controller
 */
@RestController
@RequestMapping("/system/schedule")
public class ClassroomScheduleController extends BaseController
{
    @Autowired
    private IClassroomScheduleService classroomScheduleService;

    /**
     * 查询教室时间列表
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClassroomSchedule classroomSchedule)
    {
        startPage();
        List<ClassroomSchedule> list = classroomScheduleService.selectClassroomScheduleList(classroomSchedule);
        return getDataTable(list);
    }

    /**
     * 导出教室时间列表
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:export')")
    @Log(title = "教室时间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClassroomSchedule classroomSchedule)
    {
        List<ClassroomSchedule> list = classroomScheduleService.selectClassroomScheduleList(classroomSchedule);
        ExcelUtil<ClassroomSchedule> util = new ExcelUtil<ClassroomSchedule>(ClassroomSchedule.class);
        util.exportExcel(response, list, "教室时间数据");
    }

    /**
     * 获取教室时间详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:query')")
    @GetMapping(value = "/{scheduleId}")
    public AjaxResult getInfo(@PathVariable("scheduleId") String scheduleId)
    {
        return success(classroomScheduleService.selectClassroomScheduleByScheduleId(scheduleId));
    }

    /**
     * 新增教室时间
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:add')")
    @Log(title = "教室时间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClassroomSchedule classroomSchedule)
    {
        return toAjax(classroomScheduleService.insertClassroomSchedule(classroomSchedule));
    }

    /**
     * 修改教室时间
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:edit')")
    @Log(title = "教室时间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClassroomSchedule classroomSchedule)
    {
        return toAjax(classroomScheduleService.updateClassroomSchedule(classroomSchedule));
    }

    /**
     * 删除教室时间
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:remove')")
    @Log(title = "教室时间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{scheduleIds}")
    public AjaxResult remove(@PathVariable String[] scheduleIds)
    {
        return toAjax(classroomScheduleService.deleteClassroomScheduleByScheduleIds(scheduleIds));
    }
}

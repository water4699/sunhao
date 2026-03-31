package com.ruoyi.subject.controller;

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
import com.ruoyi.subject.domain.Subject;
import com.ruoyi.subject.service.ISubjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 科目Controller
 */
@RestController
@RequestMapping("/system/subject")
public class SubjectController extends BaseController
{
    @Autowired
    private ISubjectService subjectService;

    /**
     * 查询科目列表
     */
    @PreAuthorize("@ss.hasPermi('system:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(Subject subject)
    {
        startPage();
        List<Subject> list = subjectService.selectSubjectList(subject);
        return getDataTable(list);
    }

    /**
     * 导出科目列表
     */
    @PreAuthorize("@ss.hasPermi('system:subject:export')")
    @Log(title = "科目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Subject subject)
    {
        List<Subject> list = subjectService.selectSubjectList(subject);
        ExcelUtil<Subject> util = new ExcelUtil<Subject>(Subject.class);
        util.exportExcel(response, list, "科目数据");
    }

    /**
     * 获取科目详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:subject:query')")
    @GetMapping(value = "/{subjectId}")
    public AjaxResult getInfo(@PathVariable("subjectId") String subjectId)
    {
        return success(subjectService.selectSubjectBySubjectId(subjectId));
    }

    /**
     * 新增科目
     */
    @PreAuthorize("@ss.hasPermi('system:subject:add')")
    @Log(title = "科目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Subject subject)
    {
        return toAjax(subjectService.insertSubject(subject));
    }

    /**
     * 修改科目
     */
    @PreAuthorize("@ss.hasPermi('system:subject:edit')")
    @Log(title = "科目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Subject subject)
    {
        return toAjax(subjectService.updateSubject(subject));
    }

    /**
     * 删除科目
     */
    @PreAuthorize("@ss.hasPermi('system:subject:remove')")
    @Log(title = "科目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{subjectIds}")
    public AjaxResult remove(@PathVariable String[] subjectIds)
    {
        return toAjax(subjectService.deleteSubjectBySubjectIds(subjectIds));
    }

    @PreAuthorize("@ss.hasPermi('system:subject:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<Subject> list = subjectService.selectSubjectList(new Subject());
        return AjaxResult.success(list);
    }

}

package com.ruoyi.grade.controller;

import java.util.List;
import java.util.stream.Collectors;
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
import com.ruoyi.grade.domain.GradeLevel;
import com.ruoyi.grade.service.IGradeLevelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 年级Controller
 */
@RestController
@RequestMapping("/system/level")
public class GradeLevelController extends BaseController
{
    @Autowired
    private IGradeLevelService gradeLevelService;

    /**
     * 查询年级列表
     */
    @PreAuthorize("@ss.hasPermi('system:level:list')")
    @GetMapping("/list")
    public TableDataInfo list(GradeLevel gradeLevel)
    {
        startPage();
        List<GradeLevel> list = gradeLevelService.selectGradeLevelList(gradeLevel);
        return getDataTable(list);
    }

    /**
     * 导出年级列表
     */
    @PreAuthorize("@ss.hasPermi('system:level:export')")
    @Log(title = "年级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GradeLevel gradeLevel)
    {
        List<GradeLevel> list = gradeLevelService.selectGradeLevelList(gradeLevel);
        ExcelUtil<GradeLevel> util = new ExcelUtil<GradeLevel>(GradeLevel.class);
        util.exportExcel(response, list, "年级数据");
    }

    /**
     * 获取年级详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:level:query')")
    @GetMapping(value = "/{gradeId}")
    public AjaxResult getInfo(@PathVariable("gradeId") String gradeId)
    {
        return success(gradeLevelService.selectGradeLevelByGradeId(gradeId));
    }

    /**
     * 新增年级
     */
    @PreAuthorize("@ss.hasPermi('system:level:add')")
    @Log(title = "年级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GradeLevel gradeLevel)
    {
        return toAjax(gradeLevelService.insertGradeLevel(gradeLevel));
    }

    /**
     * 修改年级
     */
    @PreAuthorize("@ss.hasPermi('system:level:edit')")
    @Log(title = "年级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GradeLevel gradeLevel)
    {
        return toAjax(gradeLevelService.updateGradeLevel(gradeLevel));
    }

    /**
     * 删除年级
     */
    @PreAuthorize("@ss.hasPermi('system:level:remove')")
    @Log(title = "年级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gradeIds}")
    public AjaxResult remove(@PathVariable String[] gradeIds)
    {
        return toAjax(gradeLevelService.deleteGradeLevelByGradeIds(gradeIds));
    }


    // 添加获取年级名称列表的接口
    @PreAuthorize("@ss.hasPermi('system:level:list')")
    @GetMapping("/nameList")
    public AjaxResult getNameList() {
        List<GradeLevel> list = gradeLevelService.selectGradeLevelList(new GradeLevel());
        List<Object> gradeNames = list.stream()
                .map(GradeLevel::getGradeName)
                .collect(Collectors.toList());
        return success(gradeNames);
    }

    @PreAuthorize("@ss.hasPermi('system:level:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<GradeLevel> list = gradeLevelService.selectGradeLevelList(new GradeLevel());
        return AjaxResult.success(list);
    }

}

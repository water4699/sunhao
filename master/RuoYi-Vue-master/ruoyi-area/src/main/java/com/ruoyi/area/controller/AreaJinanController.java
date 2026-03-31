package com.ruoyi.area.controller;

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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.area.domain.AreaJinan;
import com.ruoyi.area.service.IAreaJinanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 济南地区Controller
 */
@RestController
@RequestMapping("/system/jinan")
public class AreaJinanController extends BaseController
{
    @Autowired
    private IAreaJinanService areaJinanService;

    /**
     * 查询济南地区列表
     */
    @PreAuthorize("@ss.hasPermi('system:jinan:list')")
    @GetMapping("/list")
    public TableDataInfo list(AreaJinan areaJinan)
    {
        startPage();
        List<AreaJinan> list = areaJinanService.selectAreaJinanList(areaJinan);
        return getDataTable(list);
    }

    /**
     * 导出济南地区列表
     */
    @PreAuthorize("@ss.hasPermi('system:jinan:export')")
    @Log(title = "济南地区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AreaJinan areaJinan)
    {
        List<AreaJinan> list = areaJinanService.selectAreaJinanList(areaJinan);
        ExcelUtil<AreaJinan> util = new ExcelUtil<AreaJinan>(AreaJinan.class);
        util.exportExcel(response, list, "济南地区数据");
    }

    /**
     * 获取济南地区详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:jinan:query')")
    @GetMapping(value = "/{areaId}")
    public AjaxResult getInfo(@PathVariable("areaId") String areaId)
    {
        return success(areaJinanService.selectAreaJinanByAreaId(areaId));
    }

    /**
     * 新增济南地区
     */
    @PreAuthorize("@ss.hasPermi('system:jinan:add')")
    @Log(title = "济南地区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AreaJinan areaJinan)
    {
        areaJinan.setCreatedAt(new Date());
        return toAjax(areaJinanService.insertAreaJinan(areaJinan));
    }

    /**
     * 修改济南地区
     */
    @PreAuthorize("@ss.hasPermi('system:jinan:edit')")
    @Log(title = "济南地区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AreaJinan areaJinan)
    {
        return toAjax(areaJinanService.updateAreaJinan(areaJinan));
    }

    /**
     * 删除济南地区
     */
    @PreAuthorize("@ss.hasPermi('system:jinan:remove')")
    @Log(title = "济南地区", businessType = BusinessType.DELETE)
	@DeleteMapping("/{areaIds}")
    public AjaxResult remove(@PathVariable String[] areaIds)
    {
        return toAjax(areaJinanService.deleteAreaJinanByAreaIds(areaIds));
    }

    @PreAuthorize("@ss.hasPermi('system:jinan:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<AreaJinan> list = areaJinanService.selectAreaJinanList(new AreaJinan());
        return AjaxResult.success(list);
    }
}

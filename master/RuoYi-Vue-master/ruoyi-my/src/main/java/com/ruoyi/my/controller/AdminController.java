package com.ruoyi.my.controller;

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
import com.ruoyi.my.domain.Admin;
import com.ruoyi.my.service.IAdminService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 管理员Controller
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController extends BaseController
{
    @Autowired
    private IAdminService adminService;

    /**
     * 查询管理员列表
     */
    @PreAuthorize("@ss.hasPermi('system:admin:list')")
    @GetMapping("/list")
    public TableDataInfo list(Admin admin)
    {
        startPage();
        List<Admin> list = adminService.selectAdminList(admin);
        return getDataTable(list);
    }

    /**
     * 导出管理员列表
     */
    @PreAuthorize("@ss.hasPermi('system:admin:export')")
    @Log(title = "管理员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Admin admin)
    {
        List<Admin> list = adminService.selectAdminList(admin);
        ExcelUtil<Admin> util = new ExcelUtil<Admin>(Admin.class);
        util.exportExcel(response, list, "管理员数据");
    }

    /**
     * 获取管理员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:admin:query')")
    @GetMapping(value = "/{adminId}")
    public AjaxResult getInfo(@PathVariable("adminId") String adminId)
    {
        return success(adminService.selectAdminByAdminId(adminId));
    }

    /**
     * 新增管理员
     */
    @PreAuthorize("@ss.hasPermi('system:admin:add')")
    @Log(title = "管理员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Admin admin)
    {
        return toAjax(adminService.insertAdmin(admin));
    }

    /**
     * 修改管理员
     */
    @PreAuthorize("@ss.hasPermi('system:admin:edit')")
    @Log(title = "管理员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Admin admin)
    {
        return toAjax(adminService.updateAdmin(admin));
    }

    /**
     * 删除管理员
     */
    @PreAuthorize("@ss.hasPermi('system:admin:remove')")
    @Log(title = "管理员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{adminIds}")
    public AjaxResult remove(@PathVariable String[] adminIds)
    {
        return toAjax(adminService.deleteAdminByAdminIds(adminIds));
    }
}

package com.ruoyi.notification.controller;

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
import com.ruoyi.notification.domain.Notification;
import com.ruoyi.notification.service.INotificationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通知Controller
 */
@RestController
@RequestMapping("/system/notification")
public class NotificationController extends BaseController
{
    @Autowired
    private INotificationService notificationService;

    /**
     * 查询通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:notification:list')")
    @GetMapping("/list")
    public TableDataInfo list(Notification notification)
    {
        startPage();
        List<Notification> list = notificationService.selectNotificationList(notification);
        return getDataTable(list);
    }

    /**
     * 导出通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:notification:export')")
    @Log(title = "通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Notification notification)
    {
        List<Notification> list = notificationService.selectNotificationList(notification);
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification.class);
        util.exportExcel(response, list, "通知数据");
    }

    /**
     * 获取通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notification:query')")
    @GetMapping(value = "/{notificationId}")
    public AjaxResult getInfo(@PathVariable("notificationId") String notificationId)
    {
        return success(notificationService.selectNotificationByNotificationId(notificationId));
    }

    /**
     * 新增通知
     */
    @PreAuthorize("@ss.hasPermi('system:notification:add')")
    @Log(title = "通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Notification notification)
    {
        return toAjax(notificationService.insertNotification(notification));
    }

    /**
     * 修改通知
     */
    @PreAuthorize("@ss.hasPermi('system:notification:edit')")
    @Log(title = "通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Notification notification)
    {
        return toAjax(notificationService.updateNotification(notification));
    }

    /**
     * 删除通知
     */
    @PreAuthorize("@ss.hasPermi('system:notification:remove')")
    @Log(title = "通知", businessType = BusinessType.DELETE)
	@DeleteMapping("/{notificationIds}")
    public AjaxResult remove(@PathVariable String[] notificationIds)
    {
        return toAjax(notificationService.deleteNotificationByNotificationIds(notificationIds));
    }
}

package com.ruoyi.notification.service;

import java.util.List;
import com.ruoyi.notification.domain.Notification;

/**
 * 通知Service接口
 */
public interface INotificationService 
{
    /**
     * 查询通知
     * 
     * @param notificationId 通知主键
     * @return 通知
     */
    public Notification selectNotificationByNotificationId(String notificationId);

    /**
     * 查询通知列表
     * 
     * @param notification 通知
     * @return 通知集合
     */
    public List<Notification> selectNotificationList(Notification notification);

    /**
     * 新增通知
     * 
     * @param notification 通知
     * @return 结果
     */
    public int insertNotification(Notification notification);

    /**
     * 修改通知
     * 
     * @param notification 通知
     * @return 结果
     */
    public int updateNotification(Notification notification);

    /**
     * 批量删除通知
     * 
     * @param notificationIds 需要删除的通知主键集合
     * @return 结果
     */
    public int deleteNotificationByNotificationIds(String[] notificationIds);

    /**
     * 删除通知信息
     * 
     * @param notificationId 通知主键
     * @return 结果
     */
    public int deleteNotificationByNotificationId(String notificationId);
}

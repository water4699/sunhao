package com.ruoyi.notification.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.notification.mapper.NotificationMapper;
import com.ruoyi.notification.domain.Notification;
import com.ruoyi.notification.service.INotificationService;

/**
 * 通知Service业务层处理
 */
@Service
public class NotificationServiceImpl implements INotificationService 
{
    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 查询通知
     * 
     * @param notificationId 通知主键
     * @return 通知
     */
    @Override
    public Notification selectNotificationByNotificationId(String notificationId)
    {
        return notificationMapper.selectNotificationByNotificationId(notificationId);
    }

    /**
     * 查询通知列表
     * 
     * @param notification 通知
     * @return 通知
     */
    @Override
    public List<Notification> selectNotificationList(Notification notification)
    {
        return notificationMapper.selectNotificationList(notification);
    }

    /**
     * 新增通知
     * 
     * @param notification 通知
     * @return 结果
     */
    @Override
    public int insertNotification(Notification notification)
    {
        return notificationMapper.insertNotification(notification);
    }

    /**
     * 修改通知
     * 
     * @param notification 通知
     * @return 结果
     */
    @Override
    public int updateNotification(Notification notification)
    {
        return notificationMapper.updateNotification(notification);
    }

    /**
     * 批量删除通知
     * 
     * @param notificationIds 需要删除的通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationByNotificationIds(String[] notificationIds)
    {
        return notificationMapper.deleteNotificationByNotificationIds(notificationIds);
    }

    /**
     * 删除通知信息
     * 
     * @param notificationId 通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationByNotificationId(String notificationId)
    {
        return notificationMapper.deleteNotificationByNotificationId(notificationId);
    }
}

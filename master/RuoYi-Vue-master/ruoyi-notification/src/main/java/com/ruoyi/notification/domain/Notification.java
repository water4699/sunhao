package com.ruoyi.notification.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通知对象 notification
 */
public class Notification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String notificationId;

    /** 通知标题 */
    @Excel(name = "通知标题")
    private String title;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String content;

    /** 通知类型 */
    @Excel(name = "通知类型")
    private String type;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sentAt;

    public void setNotificationId(String notificationId) 
    {
        this.notificationId = notificationId;
    }

    public String getNotificationId() 
    {
        return notificationId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setSentAt(Date sentAt) 
    {
        this.sentAt = sentAt;
    }

    public Date getSentAt() 
    {
        return sentAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("notificationId", getNotificationId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("type", getType())
            .append("status", getStatus())
            .append("sentAt", getSentAt())
            .toString();
    }
}

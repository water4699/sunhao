package com.ruoyi.schedule.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教室时间对象 classroom_schedule
 */
public class ClassroomSchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String scheduleId;

    /** 教室ID */
    @Excel(name = "教室ID")
    private String classroomId;

    /** 星期几(1-7) */
    @Excel(name = "星期几(1-7)")
    private Integer weekday;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setScheduleId(String scheduleId) 
    {
        this.scheduleId = scheduleId;
    }

    public String getScheduleId() 
    {
        return scheduleId;
    }

    public void setClassroomId(String classroomId) 
    {
        this.classroomId = classroomId;
    }

    public String getClassroomId() 
    {
        return classroomId;
    }

    public void setWeekday(Integer weekday) 
    {
        this.weekday = weekday;
    }

    public Integer getWeekday() 
    {
        return weekday;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("scheduleId", getScheduleId())
            .append("classroomId", getClassroomId())
            .append("weekday", getWeekday())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

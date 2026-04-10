package com.ruoyi.course.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 老师对需求单接单记录
 */
public class TutorRequestBid
{
    private String bidId;
    private String requestId;
    private String teacherId;
    private String message;
    private BigDecimal expectedRate;
    /** 0待家长确认 1已确认 2已拒绝 3已撤回 */
    private Long status;
    private Date createdAt;

    private String teacherName;
    private String teacherImage;
    private String teacherEducation;
    private String teacherUniversity;
    private String requestUserId;

    public String getBidId()
    {
        return bidId;
    }

    public void setBidId(String bidId)
    {
        this.bidId = bidId;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getTeacherId()
    {
        return teacherId;
    }

    public void setTeacherId(String teacherId)
    {
        this.teacherId = teacherId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public BigDecimal getExpectedRate()
    {
        return expectedRate;
    }

    public void setExpectedRate(BigDecimal expectedRate)
    {
        this.expectedRate = expectedRate;
    }

    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public void setTeacherName(String teacherName)
    {
        this.teacherName = teacherName;
    }

    public String getTeacherImage()
    {
        return teacherImage;
    }

    public void setTeacherImage(String teacherImage)
    {
        this.teacherImage = teacherImage;
    }

    public String getTeacherEducation()
    {
        return teacherEducation;
    }

    public void setTeacherEducation(String teacherEducation)
    {
        this.teacherEducation = teacherEducation;
    }

    public String getTeacherUniversity()
    {
        return teacherUniversity;
    }

    public void setTeacherUniversity(String teacherUniversity)
    {
        this.teacherUniversity = teacherUniversity;
    }

    public String getRequestUserId()
    {
        return requestUserId;
    }

    public void setRequestUserId(String requestUserId)
    {
        this.requestUserId = requestUserId;
    }
}

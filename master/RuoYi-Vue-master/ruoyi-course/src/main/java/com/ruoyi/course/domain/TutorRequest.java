package com.ruoyi.course.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 家长发布找家教需求单
 */
public class TutorRequest
{
    private String requestId;
    private String userId;
    private String subjectId;
    private String gradeId;
    private String areaId;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String contactInfo;
    private String demandDesc;
    private String teachMode;
    private String preferredTime;
    /** 0待接单 1已接单 2已关闭 3已取消 */
    private Long status;
    private Date createdAt;

    private String parentName;
    private String subjectName;
    private String gradeName;
    private String areaName;

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getGradeId()
    {
        return gradeId;
    }

    public void setGradeId(String gradeId)
    {
        this.gradeId = gradeId;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public BigDecimal getBudgetMin()
    {
        return budgetMin;
    }

    public void setBudgetMin(BigDecimal budgetMin)
    {
        this.budgetMin = budgetMin;
    }

    public BigDecimal getBudgetMax()
    {
        return budgetMax;
    }

    public void setBudgetMax(BigDecimal budgetMax)
    {
        this.budgetMax = budgetMax;
    }

    public String getContactInfo()
    {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo)
    {
        this.contactInfo = contactInfo;
    }

    public String getDemandDesc()
    {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc)
    {
        this.demandDesc = demandDesc;
    }

    public String getTeachMode()
    {
        return teachMode;
    }

    public void setTeachMode(String teachMode)
    {
        this.teachMode = teachMode;
    }

    public String getPreferredTime()
    {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime)
    {
        this.preferredTime = preferredTime;
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

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }

    public String getGradeName()
    {
        return gradeName;
    }

    public void setGradeName(String gradeName)
    {
        this.gradeName = gradeName;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }
}

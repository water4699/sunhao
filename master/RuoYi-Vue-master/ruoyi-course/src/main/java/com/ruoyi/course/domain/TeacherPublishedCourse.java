package com.ruoyi.course.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 老师发布课程对象（与学生预约 course 表分离）
 */
public class TeacherPublishedCourse
{
    private String publishId;
    private String teacherId;
    private String subjectId;
    private String gradeId;
    private Long status;
    private Date startDate;
    private String address;
    private String expectedHours;
    private BigDecimal hourlyRate;
    private Date createdAt;
    private String teacherName;
    private String teacherImage;
    private String subjectName;
    private String gradeName;

    public String getPublishId()
    {
        return publishId;
    }

    public void setPublishId(String publishId)
    {
        this.publishId = publishId;
    }

    public String getTeacherId()
    {
        return teacherId;
    }

    public void setTeacherId(String teacherId)
    {
        this.teacherId = teacherId;
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

    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getExpectedHours()
    {
        return expectedHours;
    }

    public void setExpectedHours(String expectedHours)
    {
        this.expectedHours = expectedHours;
    }

    public BigDecimal getHourlyRate()
    {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate)
    {
        this.hourlyRate = hourlyRate;
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
}

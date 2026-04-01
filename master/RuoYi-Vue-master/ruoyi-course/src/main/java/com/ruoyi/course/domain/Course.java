package com.ruoyi.course.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程管理对象 course
 */
public class Course extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String courseId;

    /** 学生ID */
    @Excel(name = "学生ID")
    private String studentId;

    /** 教师ID */
    @Excel(name = "教师ID")
    private String teacherId;

    /** 科目ID */
    @Excel(name = "科目ID")
    private String subjectId;

    /** 年级ID */
    @Excel(name = "年级ID")
    private String gradeId;

    /** 课程状态 */
    @Excel(name = "课程状态")
    private Long status;

    /** 开始日期（小程序预约可与 address 中「时段」配合使用） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 上课地址 */
    @Excel(name = "上课地址")
    private String address;

    /** 预期课时 */
    @Excel(name = "预期课时")
    private String expectedHours;

    /** 已完成课时 */
    @Excel(name = "已完成课时")
    private String completedHours;

    /** 课时费用 */
    @Excel(name = "课时费用")
    private BigDecimal hourlyRate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setCourseId(String courseId) 
    {
        this.courseId = courseId;
    }

    public String getCourseId() 
    {
        return courseId;
    }

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setTeacherId(String teacherId) 
    {
        this.teacherId = teacherId;
    }

    public String getTeacherId() 
    {
        return teacherId;
    }

    public void setSubjectId(String subjectId) 
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId() 
    {
        return subjectId;
    }

    public void setGradeId(String gradeId) 
    {
        this.gradeId = gradeId;
    }

    public String getGradeId() 
    {
        return gradeId;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setExpectedHours(String expectedHours) 
    {
        this.expectedHours = expectedHours;
    }

    public String getExpectedHours() 
    {
        return expectedHours;
    }

    public void setCompletedHours(String completedHours) 
    {
        this.completedHours = completedHours;
    }

    public String getCompletedHours() 
    {
        return completedHours;
    }

    public void setHourlyRate(BigDecimal hourlyRate) 
    {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getHourlyRate() 
    {
        return hourlyRate;
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
            .append("courseId", getCourseId())
            .append("studentId", getStudentId())
            .append("teacherId", getTeacherId())
            .append("subjectId", getSubjectId())
            .append("gradeId", getGradeId())
            .append("status", getStatus())
            .append("startDate", getStartDate())
            .append("address", getAddress())
            .append("expectedHours", getExpectedHours())
            .append("completedHours", getCompletedHours())
            .append("hourlyRate", getHourlyRate())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

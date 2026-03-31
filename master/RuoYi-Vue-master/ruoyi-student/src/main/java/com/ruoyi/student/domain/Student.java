package com.ruoyi.student.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生信息对象 student
 */
public class Student extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String studentId;

    /** 关联科目id */
    @Excel(name = "关联科目id")
    private String subjectId;

    /** 关联用户ID */
    @Excel(name = "关联用户ID")
    private String userId;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 学校 */
    @Excel(name = "学校")
    private String school;

    /** 年级 */
    @Excel(name = "年级")
    private String gradeLevel;

    /** 学习需求 */
    @Excel(name = "学习需求")
    private String learningNeeds;

    /** 预算时薪 */
    @Excel(name = "预算时薪")
    private BigDecimal budget;

    /** 学生状态 */
    @Excel(name = "学生状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date updatedAt;

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setSubjectId(String subjectId) 
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId() 
    {
        return subjectId;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setBirthDate(Date birthDate) 
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() 
    {
        return birthDate;
    }

    public void setSchool(String school) 
    {
        this.school = school;
    }

    public String getSchool() 
    {
        return school;
    }

    public void setGradeLevel(String gradeLevel) 
    {
        this.gradeLevel = gradeLevel;
    }

    public String getGradeLevel() 
    {
        return gradeLevel;
    }

    public void setLearningNeeds(String learningNeeds) 
    {
        this.learningNeeds = learningNeeds;
    }

    public String getLearningNeeds() 
    {
        return learningNeeds;
    }

    public void setBudget(BigDecimal budget) 
    {
        this.budget = budget;
    }

    public BigDecimal getBudget() 
    {
        return budget;
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

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("studentId", getStudentId())
            .append("subjectId", getSubjectId())
            .append("userId", getUserId())
            .append("realName", getRealName())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("school", getSchool())
            .append("gradeLevel", getGradeLevel())
            .append("learningNeeds", getLearningNeeds())
            .append("budget", getBudget())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}

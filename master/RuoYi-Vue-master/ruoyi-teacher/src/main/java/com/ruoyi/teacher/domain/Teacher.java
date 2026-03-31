package com.ruoyi.teacher.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教师信息对象 teacher
 */
public class Teacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String teacherId;

    /** 关联用户ID */
    @Excel(name = "关联用户ID")
    private String userId;

    /** 关联科目 */
    @Excel(name = "关联科目")
    private Integer subjectId;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 学历 */
    @Excel(name = "学历")
    private String education;

    /** 所在大学 */
    @Excel(name = "所在大学")
    private String university;

    /** 时薪 */
    @Excel(name = "时薪")
    private BigDecimal hourlyRate;

    /** 平均评分 */
    @Excel(name = "平均评分")
    private BigDecimal rating;

    /** 认证状态 */
    @Excel(name = "认证状态")
    private Long status;

    /** 图片 */
    @Excel(name = "图片")
    private String image;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creatTime;

    public void setTeacherId(String teacherId) 
    {
        this.teacherId = teacherId;
    }

    public String getTeacherId() 
    {
        return teacherId;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setSubjectId(Integer subjectId) 
    {
        this.subjectId = subjectId;
    }

    public Integer getSubjectId() 
    {
        return subjectId;
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

    public void setEducation(String education) 
    {
        this.education = education;
    }

    public String getEducation() 
    {
        return education;
    }

    public void setUniversity(String university) 
    {
        this.university = university;
    }

    public String getUniversity() 
    {
        return university;
    }

    public void setHourlyRate(BigDecimal hourlyRate) 
    {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getHourlyRate() 
    {
        return hourlyRate;
    }

    public void setRating(BigDecimal rating) 
    {
        this.rating = rating;
    }

    public BigDecimal getRating() 
    {
        return rating;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    public void setCreatTime(Date creatTime) 
    {
        this.creatTime = creatTime;
    }

    public Date getCreatTime() 
    {
        return creatTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("teacherId", getTeacherId())
            .append("userId", getUserId())
            .append("subjectId", getSubjectId())
            .append("realName", getRealName())
            .append("gender", getGender())
            .append("education", getEducation())
            .append("university", getUniversity())
            .append("hourlyRate", getHourlyRate())
            .append("rating", getRating())
            .append("status", getStatus())
            .append("image", getImage())
            .append("creatTime", getCreatTime())
            .toString();
    }
}

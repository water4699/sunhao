package com.ruoyi.grade.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 年级对象 grade_level
 */
public class GradeLevel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String gradeId;

    /** 年级名称 */
    @Excel(name = "年级名称")
    private String name;

    /** 学段 */
    @Excel(name = "学段")
    private String educationStage;

    /** 排序序号 */
    @Excel(name = "排序序号")
    private String order;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setGradeId(String gradeId) 
    {
        this.gradeId = gradeId;
    }

    public String getGradeId() 
    {
        return gradeId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setEducationStage(String educationStage) 
    {
        this.educationStage = educationStage;
    }

    public String getEducationStage() 
    {
        return educationStage;
    }

    public void setOrder(String order) 
    {
        this.order = order;
    }

    public String getOrder() 
    {
        return order;
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
            .append("gradeId", getGradeId())
            .append("name", getName())
            .append("educationStage", getEducationStage())
            .append("order", getOrder())
            .append("createdAt", getCreatedAt())
            .toString();
    }

    public Object getGradeName() {
        return gradeId;
    }
}

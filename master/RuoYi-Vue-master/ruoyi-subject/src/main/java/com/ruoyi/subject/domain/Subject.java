package com.ruoyi.subject.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 科目对象 subject
 */
public class Subject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String subjectId;

    /** 父科目ID */
    @Excel(name = "父科目ID")
    private String categoryId;

    /** 科目名称 */
    @Excel(name = "科目名称")
    private String name;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setSubjectId(String subjectId) 
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId() 
    {
        return subjectId;
    }

    public void setCategoryId(String categoryId) 
    {
        this.categoryId = categoryId;
    }

    public String getCategoryId() 
    {
        return categoryId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
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
            .append("subjectId", getSubjectId())
            .append("categoryId", getCategoryId())
            .append("name", getName())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

package com.ruoyi.area.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 济南地区对象 area_jinan
 */
public class AreaJinan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地区ID */
    private String areaId;

    /** 地区名称 */
    @Excel(name = "地区名称")
    private String name;

    /** 地区级别 */
    @Excel(name = "地区级别")
    private String level;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setAreaId(String areaId) 
    {
        this.areaId = areaId;
    }

    public String getAreaId() 
    {
        return areaId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
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
            .append("areaId", getAreaId())
            .append("name", getName())
            .append("level", getLevel())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

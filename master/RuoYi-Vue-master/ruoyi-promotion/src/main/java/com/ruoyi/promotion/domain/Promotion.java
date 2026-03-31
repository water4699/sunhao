package com.ruoyi.promotion.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 优惠活动对象 promotion
 */
public class Promotion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String promotionId;

    /** 活动标题 */
    @Excel(name = "活动标题")
    private String title;

    /** 活动描述 */
    @Excel(name = "活动描述")
    private String description;

    /** 折扣类型 */
    @Excel(name = "折扣类型")
    private String discountType;

    /** 折扣值 */
    @Excel(name = "折扣值")
    private BigDecimal discountValue;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 适用对象 */
    @Excel(name = "适用对象")
    private String applicableTo;

    /** 使用次数限制 */
    @Excel(name = "使用次数限制")
    private String usageLimit;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setPromotionId(String promotionId) 
    {
        this.promotionId = promotionId;
    }

    public String getPromotionId() 
    {
        return promotionId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setDiscountType(String discountType) 
    {
        this.discountType = discountType;
    }

    public String getDiscountType() 
    {
        return discountType;
    }

    public void setDiscountValue(BigDecimal discountValue) 
    {
        this.discountValue = discountValue;
    }

    public BigDecimal getDiscountValue() 
    {
        return discountValue;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setApplicableTo(String applicableTo) 
    {
        this.applicableTo = applicableTo;
    }

    public String getApplicableTo() 
    {
        return applicableTo;
    }

    public void setUsageLimit(String usageLimit) 
    {
        this.usageLimit = usageLimit;
    }

    public String getUsageLimit() 
    {
        return usageLimit;
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
            .append("promotionId", getPromotionId())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("discountType", getDiscountType())
            .append("discountValue", getDiscountValue())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("applicableTo", getApplicableTo())
            .append("usageLimit", getUsageLimit())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

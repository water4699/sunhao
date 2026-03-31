package com.ruoyi.card.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会员卡类型对象 card
 */
public class Card extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String cardId;

    /** 会员卡名称 */
    @Excel(name = "会员卡名称")
    private String cardName;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 有效次数
 */
    @Excel(name = "有效次数")
    private String durationCount;

    /** 折扣率 */
    @Excel(name = "折扣率")
    private BigDecimal discountRate;

    /** 卡描述 */
    @Excel(name = "卡描述")
    private String description;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setCardId(String cardId) 
    {
        this.cardId = cardId;
    }

    public String getCardId() 
    {
        return cardId;
    }

    public void setCardName(String cardName) 
    {
        this.cardName = cardName;
    }

    public String getCardName() 
    {
        return cardName;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setDurationCount(String durationCount) 
    {
        this.durationCount = durationCount;
    }

    public String getDurationCount() 
    {
        return durationCount;
    }

    public void setDiscountRate(BigDecimal discountRate) 
    {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountRate() 
    {
        return discountRate;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
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
            .append("cardId", getCardId())
            .append("cardName", getCardName())
            .append("price", getPrice())
            .append("durationCount", getDurationCount())
            .append("discountRate", getDiscountRate())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

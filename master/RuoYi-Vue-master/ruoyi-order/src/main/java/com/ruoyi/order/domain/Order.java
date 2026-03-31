package com.ruoyi.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单对象 order
 */
public class Order extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String orderId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 关联课程 */
    @Excel(name = "关联课程")
    private String courseId;

    /** 优惠活动 */
    @Excel(name = "优惠活动")
    private String promotionId;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal amount;

    /** 折扣金额 */
    @Excel(name = "折扣金额")
    private BigDecimal discountAmount;

    /** 实际支付金额 */
    @Excel(name = "实际支付金额")
    private BigDecimal finalAmount;

    /** 支付方式 */
    @Excel(name = "支付方式")
    private String paymentMethod;

    /** 支付状态 */
    @Excel(name = "支付状态")
    private Long paymentStatus;

    /** 支付平台交易ID */
    @Excel(name = "支付平台交易ID")
    private String transactionId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setCourseId(String courseId) 
    {
        this.courseId = courseId;
    }

    public String getCourseId() 
    {
        return courseId;
    }

    public void setPromotionId(String promotionId) 
    {
        this.promotionId = promotionId;
    }

    public String getPromotionId() 
    {
        return promotionId;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) 
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount() 
    {
        return discountAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) 
    {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getFinalAmount() 
    {
        return finalAmount;
    }

    public void setPaymentMethod(String paymentMethod) 
    {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() 
    {
        return paymentMethod;
    }

    public void setPaymentStatus(Long paymentStatus) 
    {
        this.paymentStatus = paymentStatus;
    }

    public Long getPaymentStatus() 
    {
        return paymentStatus;
    }

    public void setTransactionId(String transactionId) 
    {
        this.transactionId = transactionId;
    }

    public String getTransactionId() 
    {
        return transactionId;
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
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("courseId", getCourseId())
            .append("promotionId", getPromotionId())
            .append("amount", getAmount())
            .append("discountAmount", getDiscountAmount())
            .append("finalAmount", getFinalAmount())
            .append("paymentMethod", getPaymentMethod())
            .append("paymentStatus", getPaymentStatus())
            .append("transactionId", getTransactionId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}

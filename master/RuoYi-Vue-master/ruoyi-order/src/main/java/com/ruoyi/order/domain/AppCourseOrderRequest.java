package com.ruoyi.order.domain;

public class AppCourseOrderRequest
{
    private String publishId;
    private Integer quantity;

    public String getPublishId()
    {
        return publishId;
    }

    public void setPublishId(String publishId)
    {
        this.publishId = publishId;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }
}

package com.ruoyi.order.domain;

/**
 * 小程序创建商品订单请求体（模拟支付）
 */
public class AppProductOrderRequest
{
    private String productId;
    private Integer quantity;

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public Integer getQuantity()
    {
        return quantity == null ? 1 : quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }
}

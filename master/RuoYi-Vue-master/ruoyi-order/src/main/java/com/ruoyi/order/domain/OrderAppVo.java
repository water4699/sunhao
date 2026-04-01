package com.ruoyi.order.domain;

/**
 * 小程序订单列表：在 {@link Order} 基础上附带商品名称（联表 product）
 */
public class OrderAppVo extends Order
{
    private static final long serialVersionUID = 1L;

    /** 商品名称，来自 product.name；无关联时为空 */
    private String productName;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }
}

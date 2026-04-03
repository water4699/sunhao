package com.ruoyi.order.domain;

/**
 * 小程序订单列表：在 {@link Order} 基础上附带商品名称（联表 product）
 */
public class OrderAppVo extends Order
{
    private static final long serialVersionUID = 1L;

    /** 商品名称，来自 product.name；无关联时为空 */
    private String productName;
    /** 学习课程名称，来自 teacher_published_course + 年级/科目 */
    private String courseName;
    /** 订单类型：product/course */
    private String itemType;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getItemType()
    {
        return itemType;
    }

    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }
}

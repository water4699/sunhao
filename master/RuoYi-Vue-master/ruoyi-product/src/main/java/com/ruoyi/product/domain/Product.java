package com.ruoyi.product.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品对象 product
 */
public class Product extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String productId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String name;

    /** 商品类型 */
    @Excel(name = "商品类型")
    private String type;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 库存数量(NULL表示无限库存) */
    @Excel(name = "库存数量(NULL表示无限库存)")
    private Long stock;

    /** 商品描述 */
    @Excel(name = "商品描述")
    private String description;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    /** 图片 */
    @Excel(name = "图片")
    private String image;

    public void setProductId(String productId) 
    {
        this.productId = productId;
    }

    public String getProductId() 
    {
        return productId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
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

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("name", getName())
            .append("type", getType())
            .append("price", getPrice())
            .append("stock", getStock())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .append("image", getImage())
            .toString();
    }
}

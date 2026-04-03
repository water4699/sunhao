package com.ruoyi.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.order.mapper.OrderMapper;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.domain.OrderAppVo;
import com.ruoyi.order.service.IOrderService;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.service.IProductService;

/**
 * 订单Service业务层处理
 *
 */
@Service
public class OrderServiceImpl implements IOrderService 
{
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IProductService productService;

    /**
     * 查询订单
     * 
     * @param orderId 订单主键
     * @return 订单
     */
    @Override
    public Order selectOrderByOrderId(String orderId)
    {
        return orderMapper.selectOrderByOrderId(orderId);
    }

    /**
     * 查询订单列表
     * 
     * @param order 订单
     * @return 订单
     */
    @Override
    public List<Order> selectOrderList(Order order)
    {
        return orderMapper.selectOrderList(order);
    }

    /**
     * 新增订单
     * 
     * @param order 订单
     * @return 结果
     */
    @Override
    public int insertOrder(Order order)
    {
        return orderMapper.insertOrder(order);
    }

    /**
     * 修改订单
     * 
     * @param order 订单
     * @return 结果
     */
    @Override
    public int updateOrder(Order order)
    {
        return orderMapper.updateOrder(order);
    }

    /**
     * 批量删除订单
     * 
     * @param orderIds 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderByOrderIds(String[] orderIds)
    {
        return orderMapper.deleteOrderByOrderIds(orderIds);
    }

    /**
     * 删除订单信息
     * 
     * @param orderId 订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderByOrderId(String orderId)
    {
        return orderMapper.deleteOrderByOrderId(orderId);
    }

    @Override
    public String createProductOrderForUser(Long userId, String productId, Integer quantity)
    {
        if (userId == null)
        {
            throw new ServiceException("请先登录");
        }
        if (StringUtils.isEmpty(productId))
        {
            throw new ServiceException("请选择商品");
        }
        int q = quantity == null ? 1 : quantity;
        if (q < 1)
        {
            throw new ServiceException("购买数量至少为 1");
        }
        Product p = productService.selectProductByProductId(productId);
        if (p == null)
        {
            throw new ServiceException("商品不存在");
        }
        if (p.getStatus() != null && p.getStatus() == 0L)
        {
            throw new ServiceException("商品已下架");
        }
        if (p.getStock() != null && p.getStock() < q)
        {
            throw new ServiceException("库存不足");
        }
        BigDecimal unit = p.getPrice() != null ? p.getPrice() : BigDecimal.ZERO;
        BigDecimal total = unit.multiply(BigDecimal.valueOf(q));
        Order order = new Order();
        order.setUserId(String.valueOf(userId));
        order.setCourseId(productId);
        order.setAmount(total);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(total);
        // 库表 payment_method 为 enum('alipay','wechat','bank','cash','credit')，不可用 mock
        order.setPaymentMethod("wechat");
        order.setPaymentStatus(1L);
        order.setTransactionId("MOCK-" + System.currentTimeMillis());
        order.setCreatedAt(new Date());
        int rows = orderMapper.insertOrder(order);
        if (rows <= 0)
        {
            throw new ServiceException("下单失败");
        }
        if (p.getStock() != null)
        {
            p.setStock(p.getStock() - q);
            productService.updateProduct(p);
        }
        return order.getOrderId();
    }

    @Override
    public String createPublishedCourseOrderForUser(Long userId, String publishId, Integer quantity)
    {
        if (userId == null)
        {
            throw new ServiceException("请先登录");
        }
        if (StringUtils.isEmpty(publishId))
        {
            throw new ServiceException("请选择课程");
        }
        int q = quantity == null ? 1 : quantity;
        if (q < 1)
        {
            throw new ServiceException("购买数量至少为 1");
        }
        if (orderMapper.countPublishedCourseById(publishId) <= 0)
        {
            throw new ServiceException("课程不存在");
        }
        Long st = orderMapper.selectPublishedCourseStatusById(publishId);
        if (st != null && st != 0L)
        {
            throw new ServiceException("课程已下架");
        }
        BigDecimal unit = orderMapper.selectPublishedCoursePriceById(publishId);
        if (unit == null)
        {
            unit = BigDecimal.ZERO;
        }
        BigDecimal total = unit.multiply(BigDecimal.valueOf(q));
        Order order = new Order();
        order.setUserId(String.valueOf(userId));
        order.setCourseId(publishId);
        order.setAmount(total);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(total);
        order.setPaymentMethod("wechat");
        order.setPaymentStatus(1L);
        order.setTransactionId("COURSE-" + System.currentTimeMillis());
        order.setCreatedAt(new Date());
        int rows = orderMapper.insertOrder(order);
        if (rows <= 0)
        {
            throw new ServiceException("下单失败");
        }
        return order.getOrderId();
    }

    @Override
    public List<OrderAppVo> selectAppOrderListByUserId(String userId)
    {
        return orderMapper.selectAppOrderListByUserId(userId);
    }
}

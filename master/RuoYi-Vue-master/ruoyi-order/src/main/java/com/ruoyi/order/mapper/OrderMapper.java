package com.ruoyi.order.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.domain.OrderAppVo;

/**
 * 订单Mapper接口
 */
public interface OrderMapper 
{
    /**
     * 查询订单
     * 
     * @param orderId 订单主键
     * @return 订单
     */
    public Order selectOrderByOrderId(String orderId);

    /**
     * 查询订单列表
     * 
     * @param order 订单
     * @return 订单集合
     */
    public List<Order> selectOrderList(Order order);

    /**
     * 新增订单
     * 
     * @param order 订单
     * @return 结果
     */
    public int insertOrder(Order order);

    /**
     * 修改订单
     * 
     * @param order 订单
     * @return 结果
     */
    public int updateOrder(Order order);

    /**
     * 删除订单
     * 
     * @param orderId 订单主键
     * @return 结果
     */
    public int deleteOrderByOrderId(String orderId);

    /**
     * 批量删除订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderByOrderIds(String[] orderIds);

    /**
     * 小程序：当前用户订单列表，左联商品名称
     */
    List<OrderAppVo> selectAppOrderListByUserId(@Param("userId") String userId);
}

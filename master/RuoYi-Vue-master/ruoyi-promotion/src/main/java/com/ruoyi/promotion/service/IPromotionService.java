package com.ruoyi.promotion.service;

import java.util.List;
import com.ruoyi.promotion.domain.Promotion;

/**
 * 优惠活动Service接口
 */
public interface IPromotionService 
{
    /**
     * 查询优惠活动
     * 
     * @param promotionId 优惠活动主键
     * @return 优惠活动
     */
    public Promotion selectPromotionByPromotionId(String promotionId);

    /**
     * 查询优惠活动列表
     * 
     * @param promotion 优惠活动
     * @return 优惠活动集合
     */
    public List<Promotion> selectPromotionList(Promotion promotion);

    /**
     * 新增优惠活动
     * 
     * @param promotion 优惠活动
     * @return 结果
     */
    public int insertPromotion(Promotion promotion);

    /**
     * 修改优惠活动
     * 
     * @param promotion 优惠活动
     * @return 结果
     */
    public int updatePromotion(Promotion promotion);

    /**
     * 批量删除优惠活动
     * 
     * @param promotionIds 需要删除的优惠活动主键集合
     * @return 结果
     */
    public int deletePromotionByPromotionIds(String[] promotionIds);

    /**
     * 删除优惠活动信息
     * 
     * @param promotionId 优惠活动主键
     * @return 结果
     */
    public int deletePromotionByPromotionId(String promotionId);
}

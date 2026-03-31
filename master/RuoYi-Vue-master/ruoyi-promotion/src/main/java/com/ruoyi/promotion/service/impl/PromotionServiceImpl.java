package com.ruoyi.promotion.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.promotion.mapper.PromotionMapper;
import com.ruoyi.promotion.domain.Promotion;
import com.ruoyi.promotion.service.IPromotionService;

/**
 * 优惠活动Service业务层处理
 */
@Service
public class PromotionServiceImpl implements IPromotionService 
{
    @Autowired
    private PromotionMapper promotionMapper;

    /**
     * 查询优惠活动
     * 
     * @param promotionId 优惠活动主键
     * @return 优惠活动
     */
    @Override
    public Promotion selectPromotionByPromotionId(String promotionId)
    {
        return promotionMapper.selectPromotionByPromotionId(promotionId);
    }

    /**
     * 查询优惠活动列表
     * 
     * @param promotion 优惠活动
     * @return 优惠活动
     */
    @Override
    public List<Promotion> selectPromotionList(Promotion promotion)
    {
        return promotionMapper.selectPromotionList(promotion);
    }

    /**
     * 新增优惠活动
     * 
     * @param promotion 优惠活动
     * @return 结果
     */
    @Override
    public int insertPromotion(Promotion promotion)
    {
        return promotionMapper.insertPromotion(promotion);
    }

    /**
     * 修改优惠活动
     * 
     * @param promotion 优惠活动
     * @return 结果
     */
    @Override
    public int updatePromotion(Promotion promotion)
    {
        return promotionMapper.updatePromotion(promotion);
    }

    /**
     * 批量删除优惠活动
     * 
     * @param promotionIds 需要删除的优惠活动主键
     * @return 结果
     */
    @Override
    public int deletePromotionByPromotionIds(String[] promotionIds)
    {
        return promotionMapper.deletePromotionByPromotionIds(promotionIds);
    }

    /**
     * 删除优惠活动信息
     * 
     * @param promotionId 优惠活动主键
     * @return 结果
     */
    @Override
    public int deletePromotionByPromotionId(String promotionId)
    {
        return promotionMapper.deletePromotionByPromotionId(promotionId);
    }
}

package com.ruoyi.card.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.card.mapper.CardMapper;
import com.ruoyi.card.domain.Card;
import com.ruoyi.card.service.ICardService;

/**
 * 会员卡类型Service业务层处理
 */
@Service
public class CardServiceImpl implements ICardService 
{
    @Autowired
    private CardMapper cardMapper;

    /**
     * 查询会员卡类型
     * 
     * @param cardId 会员卡类型主键
     * @return 会员卡类型
     */
    @Override
    public Card selectCardByCardId(String cardId)
    {
        return cardMapper.selectCardByCardId(cardId);
    }

    /**
     * 查询会员卡类型列表
     * 
     * @param card 会员卡类型
     * @return 会员卡类型
     */
    @Override
    public List<Card> selectCardList(Card card)
    {
        return cardMapper.selectCardList(card);
    }

    /**
     * 新增会员卡类型
     * 
     * @param card 会员卡类型
     * @return 结果
     */
    @Override
    public int insertCard(Card card)
    {
        return cardMapper.insertCard(card);
    }

    /**
     * 修改会员卡类型
     * 
     * @param card 会员卡类型
     * @return 结果
     */
    @Override
    public int updateCard(Card card)
    {
        return cardMapper.updateCard(card);
    }

    /**
     * 批量删除会员卡类型
     * 
     * @param cardIds 需要删除的会员卡类型主键
     * @return 结果
     */
    @Override
    public int deleteCardByCardIds(String[] cardIds)
    {
        return cardMapper.deleteCardByCardIds(cardIds);
    }

    /**
     * 删除会员卡类型信息
     * 
     * @param cardId 会员卡类型主键
     * @return 结果
     */
    @Override
    public int deleteCardByCardId(String cardId)
    {
        return cardMapper.deleteCardByCardId(cardId);
    }
}

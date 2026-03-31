package com.ruoyi.card.service;

import java.util.List;
import com.ruoyi.card.domain.Card;

/**
 * 会员卡类型Service接口
 */
public interface ICardService 
{
    /**
     * 查询会员卡类型
     * 
     * @param cardId 会员卡类型主键
     * @return 会员卡类型
     */
    public Card selectCardByCardId(String cardId);

    /**
     * 查询会员卡类型列表
     * 
     * @param card 会员卡类型
     * @return 会员卡类型集合
     */
    public List<Card> selectCardList(Card card);

    /**
     * 新增会员卡类型
     * 
     * @param card 会员卡类型
     * @return 结果
     */
    public int insertCard(Card card);

    /**
     * 修改会员卡类型
     * 
     * @param card 会员卡类型
     * @return 结果
     */
    public int updateCard(Card card);

    /**
     * 批量删除会员卡类型
     * 
     * @param cardIds 需要删除的会员卡类型主键集合
     * @return 结果
     */
    public int deleteCardByCardIds(String[] cardIds);

    /**
     * 删除会员卡类型信息
     * 
     * @param cardId 会员卡类型主键
     * @return 结果
     */
    public int deleteCardByCardId(String cardId);
}

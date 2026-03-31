package com.ruoyi.card.mapper;

import java.util.List;
import com.ruoyi.card.domain.Card;

/**
 * 会员卡类型Mapper接口
 */
public interface CardMapper 
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
     * 删除会员卡类型
     * 
     * @param cardId 会员卡类型主键
     * @return 结果
     */
    public int deleteCardByCardId(String cardId);

    /**
     * 批量删除会员卡类型
     * 
     * @param cardIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCardByCardIds(String[] cardIds);
}

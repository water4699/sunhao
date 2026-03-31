package com.ruoyi.card.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.card.domain.Card;
import com.ruoyi.card.service.ICardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 会员卡类型Controller
 */
@RestController
@RequestMapping("/system/card")
public class CardController extends BaseController
{
    @Autowired
    private ICardService cardService;

    /**
     * 查询会员卡类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(Card card)
    {
        startPage();
        List<Card> list = cardService.selectCardList(card);
        return getDataTable(list);
    }

    /**
     * 导出会员卡类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:card:export')")
    @Log(title = "会员卡类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Card card)
    {
        List<Card> list = cardService.selectCardList(card);
        ExcelUtil<Card> util = new ExcelUtil<Card>(Card.class);
        util.exportExcel(response, list, "会员卡类型数据");
    }

    /**
     * 获取会员卡类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:card:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") String cardId)
    {
        return success(cardService.selectCardByCardId(cardId));
    }

    /**
     * 新增会员卡类型
     */
    @PreAuthorize("@ss.hasPermi('system:card:add')")
    @Log(title = "会员卡类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Card card)
    {
        return toAjax(cardService.insertCard(card));
    }

    /**
     * 修改会员卡类型
     */
    @PreAuthorize("@ss.hasPermi('system:card:edit')")
    @Log(title = "会员卡类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Card card)
    {
        return toAjax(cardService.updateCard(card));
    }

    /**
     * 删除会员卡类型
     */
    @PreAuthorize("@ss.hasPermi('system:card:remove')")
    @Log(title = "会员卡类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable String[] cardIds)
    {
        return toAjax(cardService.deleteCardByCardIds(cardIds));
    }
}

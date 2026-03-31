package com.ruoyi.promotion.controller;

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
import com.ruoyi.promotion.domain.Promotion;
import com.ruoyi.promotion.service.IPromotionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 优惠活动Controller
 */
@RestController
@RequestMapping("/system/promotion")
public class PromotionController extends BaseController
{
    @Autowired
    private IPromotionService promotionService;

    /**
     * 查询优惠活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:list')")
    @GetMapping("/list")
    public TableDataInfo list(Promotion promotion)
    {
        startPage();
        List<Promotion> list = promotionService.selectPromotionList(promotion);
        return getDataTable(list);
    }

    /**
     * 导出优惠活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:export')")
    @Log(title = "优惠活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Promotion promotion)
    {
        List<Promotion> list = promotionService.selectPromotionList(promotion);
        ExcelUtil<Promotion> util = new ExcelUtil<Promotion>(Promotion.class);
        util.exportExcel(response, list, "优惠活动数据");
    }

    /**
     * 获取优惠活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:query')")
    @GetMapping(value = "/{promotionId}")
    public AjaxResult getInfo(@PathVariable("promotionId") String promotionId)
    {
        return success(promotionService.selectPromotionByPromotionId(promotionId));
    }

    /**
     * 新增优惠活动
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:add')")
    @Log(title = "优惠活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Promotion promotion)
    {
        return toAjax(promotionService.insertPromotion(promotion));
    }

    /**
     * 修改优惠活动
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:edit')")
    @Log(title = "优惠活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Promotion promotion)
    {
        return toAjax(promotionService.updatePromotion(promotion));
    }

    /**
     * 删除优惠活动
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:remove')")
    @Log(title = "优惠活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{promotionIds}")
    public AjaxResult remove(@PathVariable String[] promotionIds)
    {
        return toAjax(promotionService.deletePromotionByPromotionIds(promotionIds));
    }

    @PreAuthorize("@ss.hasPermi('system:promotion:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<Promotion> list = promotionService.selectPromotionList(new Promotion());
        return AjaxResult.success(list);
    }

}

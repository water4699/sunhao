package com.ruoyi.review.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.review.domain.ReviewVo;
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
import com.ruoyi.review.domain.Review;
import com.ruoyi.review.service.IReviewService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 评价Controller
 */
@RestController
@RequestMapping("/system/review")
public class ReviewController extends BaseController
{
    @Autowired
    private IReviewService reviewService;

    /**
     * 查询评价列表
     */
    @PreAuthorize("@ss.hasPermi('system:review:list')")
    @GetMapping("/list")
    public TableDataInfo list(Review review)
    {
        startPage();
        List<Review> reviewList = reviewService.selectReviewList(review);
        List<ReviewVo> list = reviewList.stream().map(review1 -> {
            ReviewVo reviewVo = new ReviewVo();
            BeanUtils.copyBeanProp(reviewVo, review1);
            reviewVo.setStudentName(reviewService.selectStudentByStudentId(review1.getStudentId()));
            System.err.println(reviewVo);
            return reviewVo;
        }).collect(Collectors.toList());
        return getDataTable(list);
    }

    /**
     * 导出评价列表
     */
    @PreAuthorize("@ss.hasPermi('system:review:export')")
    @Log(title = "评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Review review)
    {
        List<Review> list = reviewService.selectReviewList(review);
        ExcelUtil<Review> util = new ExcelUtil<Review>(Review.class);
        util.exportExcel(response, list, "评价数据");
    }

    /**
     * 获取评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:review:query')")
    @GetMapping(value = "/{reviewId}")
    public AjaxResult getInfo(@PathVariable("reviewId") String reviewId)
    {
        return success(reviewService.selectReviewByReviewId(reviewId));
    }

    /**
     * 新增评价
     */
    @PreAuthorize("@ss.hasPermi('system:review:add')")
    @Log(title = "评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Review review)
    {
        return toAjax(reviewService.insertReview(review));
    }

    /**
     * 修改评价
     */
    @PreAuthorize("@ss.hasPermi('system:review:edit')")
    @Log(title = "评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Review review)
    {
        return toAjax(reviewService.updateReview(review));
    }

    /**
     * 删除评价
     */
    @PreAuthorize("@ss.hasPermi('system:review:remove')")
    @Log(title = "评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reviewIds}")
    public AjaxResult remove(@PathVariable String[] reviewIds)
    {
        return toAjax(reviewService.deleteReviewByReviewIds(reviewIds));
    }
}

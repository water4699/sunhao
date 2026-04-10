package com.ruoyi.course.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.course.domain.TutorRequest;
import com.ruoyi.course.domain.TutorRequestBid;
import com.ruoyi.course.service.ITutorRequestService;
import com.ruoyi.teacher.domain.Teacher;
import com.ruoyi.teacher.service.ITeacherService;

@RestController
@RequestMapping("/system/tutor-request")
public class TutorRequestController extends BaseController
{
    @Autowired
    private ITutorRequestService tutorRequestService;

    @Autowired
    private ITeacherService teacherService;

    /**
     * 管理端：需求单列表
     */
    @PreAuthorize("@ss.hasPermi('system:tutorRequest:list')")
    @GetMapping("/list")
    public TableDataInfo list(TutorRequest query)
    {
        startPage();
        List<TutorRequest> list = tutorRequestService.selectTutorRequestList(query);
        return getDataTable(list);
    }

    /**
     * 管理端：需求单详情
     */
    @PreAuthorize("@ss.hasPermi('system:tutorRequest:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        TutorRequest row = tutorRequestService.selectTutorRequestById(id);
        if (row == null)
        {
            return error("需求单不存在");
        }
        return success(row);
    }

    /**
     * 管理端：需求单接单记录
     */
    @PreAuthorize("@ss.hasPermi('system:tutorRequest:query')")
    @GetMapping("/{id}/bid/list")
    public TableDataInfo bidList(@PathVariable("id") String id)
    {
        startPage();
        List<TutorRequestBid> list = tutorRequestService.selectTutorRequestBidList(id);
        return getDataTable(list);
    }

    private LoginUser currentBusinessLoginOrThrow()
    {
        LoginUser lu = SecurityUtils.getLoginUser();
        if (lu == null || !lu.isBusinessUser())
        {
            throw new RuntimeException("请先登录");
        }
        return lu;
    }

    private String currentParentUserIdOrThrow()
    {
        LoginUser lu = currentBusinessLoginOrThrow();
        String type = StringUtils.trimToEmpty(lu.getBusinessUsersType()).toLowerCase();
        if (!"parent".equals(type) && !"student".equals(type))
        {
            throw new RuntimeException("仅家长端可操作");
        }
        return String.valueOf(lu.getUserId());
    }

    private Teacher currentApprovedTeacherOrThrow()
    {
        LoginUser lu = currentBusinessLoginOrThrow();
        String type = StringUtils.trimToEmpty(lu.getBusinessUsersType()).toLowerCase();
        if (!"teacher".equals(type))
        {
            throw new RuntimeException("仅老师账号可操作");
        }
        Teacher teacher = teacherService.selectTeacherByUserId(String.valueOf(lu.getUserId()));
        if (teacher == null)
        {
            throw new RuntimeException("请先提交老师入驻申请");
        }
        if (teacher.getStatus() == null || teacher.getStatus() != 1L)
        {
            throw new RuntimeException("入驻申请审核通过后才可操作");
        }
        return teacher;
    }

    @PostMapping("/app/create")
    public AjaxResult appCreate(@RequestBody TutorRequest body)
    {
        try
        {
            body.setUserId(currentParentUserIdOrThrow());
            return toAjax(tutorRequestService.createTutorRequest(body));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    @GetMapping("/app/my-list")
    public TableDataInfo appMyList(TutorRequest query)
    {
        try
        {
            if (query == null)
            {
                query = new TutorRequest();
            }
            query.setUserId(currentParentUserIdOrThrow());
            startPage();
            List<TutorRequest> list = tutorRequestService.selectTutorRequestList(query);
            return getDataTable(list);
        }
        catch (RuntimeException e)
        {
            return getDataTable(Collections.emptyList());
        }
    }

    @DeleteMapping("/app/{id}/close")
    public AjaxResult appClose(@PathVariable("id") String id)
    {
        try
        {
            return toAjax(tutorRequestService.closeTutorRequestByOwner(id, currentParentUserIdOrThrow()));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    @GetMapping("/app/{id}/bids")
    public TableDataInfo appBids(@PathVariable("id") String id)
    {
        try
        {
            TutorRequest req = tutorRequestService.selectTutorRequestById(id);
            if (req == null)
            {
                return getDataTable(Collections.emptyList());
            }
            if (!currentParentUserIdOrThrow().equals(req.getUserId()))
            {
                return getDataTable(Collections.emptyList());
            }
            startPage();
            List<TutorRequestBid> list = tutorRequestService.selectTutorRequestBidList(id);
            return getDataTable(list);
        }
        catch (RuntimeException e)
        {
            return getDataTable(Collections.emptyList());
        }
    }

    @PostMapping("/app/bid/{bidId}/confirm")
    public AjaxResult appConfirmBid(@PathVariable("bidId") String bidId)
    {
        try
        {
            return toAjax(tutorRequestService.confirmBidByOwner(bidId, currentParentUserIdOrThrow()));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    @PostMapping("/app/bid/{bidId}/reject")
    public AjaxResult appRejectBid(@PathVariable("bidId") String bidId)
    {
        try
        {
            return toAjax(tutorRequestService.rejectBidByOwner(bidId, currentParentUserIdOrThrow()));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    @GetMapping("/app/open-list")
    public TableDataInfo appOpenList(TutorRequest query)
    {
        if (query == null)
        {
            query = new TutorRequest();
        }
        query.setStatus(0L);
        startPage();
        List<TutorRequest> list = tutorRequestService.selectTutorRequestList(query);
        return getDataTable(list);
    }

    @PostMapping("/app/{id}/bid")
    public AjaxResult appBid(@PathVariable("id") String id, @RequestBody(required = false) TutorRequestBid body)
    {
        try
        {
            Teacher teacher = currentApprovedTeacherOrThrow();
            String message = body == null ? null : body.getMessage();
            BigDecimal expectedRate = body == null ? null : body.getExpectedRate();
            return toAjax(tutorRequestService.bidTutorRequest(id, teacher.getTeacherId(), message, expectedRate));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    @GetMapping("/app/my-bids")
    public TableDataInfo appMyBids()
    {
        try
        {
            Teacher teacher = currentApprovedTeacherOrThrow();
            startPage();
            List<TutorRequestBid> list = tutorRequestService.selectMyBidList(teacher.getTeacherId());
            return getDataTable(list);
        }
        catch (RuntimeException e)
        {
            return getDataTable(Collections.emptyList());
        }
    }
}
